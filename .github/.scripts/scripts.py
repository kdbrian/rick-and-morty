import argparse
import os
import subprocess
import sys
import time
from pathlib import Path
from typing import Optional

import requests


def load_env_file():
    """Load environment variables from .env file if it exists."""
    env_file = Path('.env')
    if env_file.exists():
        with open(env_file) as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith('#') and '=' in line:
                    key, value = line.split('=', 1)
                    if key.strip() not in os.environ:
                        os.environ[key.strip()] = value.strip().strip('"').strip("'")


def run_git_command(cmd: list[str]) -> str:
    """Execute a git command and return output."""
    try:
        result = subprocess.run(
            cmd,
            capture_output=True,
            text=True,
            check=True,
            cwd=os.getcwd()
        )
        return result.stdout.strip()
    except subprocess.CalledProcessError as e:
        print(f"❌ Git command failed: {' '.join(cmd)}", file=sys.stderr)
        print(f"Error: {e.stderr}", file=sys.stderr)
        sys.exit(1)


def get_previous_tag(current_tag: str) -> Optional[str]:
    """Get the previous version tag."""
    all_tags = run_git_command([
        'git', 'tag', '--list', 'v*.*.*', '--sort=-version:refname'
    ]).split('\n')
    prev_tags = [t for t in all_tags if t and t != current_tag]
    return prev_tags[0] if prev_tags else None


def validate_tag(tag: str) -> None:
    """Raise SystemExit if the tag does not exist locally, with helpful hints."""
    result = subprocess.run(
        ['git', 'rev-parse', '--verify', tag],
        capture_output=True, text=True
    )
    if result.returncode == 0:
        return

    # Check remote
    remote = subprocess.run(
        ['git', 'ls-remote', '--tags', 'origin', tag],
        capture_output=True, text=True
    )
    if remote.stdout.strip():
        print(f"❌ Tag '{tag}' exists on remote but not locally.", file=sys.stderr)
        print("   Run: git fetch --tags", file=sys.stderr)
        sys.exit(1)

    # List local tags to help the user
    available = subprocess.run(
        ['git', 'tag', '--list', 'v*.*.*', '--sort=-version:refname'],
        capture_output=True, text=True
    )
    tag_list = "\n  ".join(available.stdout.splitlines()[:10]) or "(none)"
    print(f"❌ Tag '{tag}' not found locally or on remote.", file=sys.stderr)
    print(f"   Available tags:\n  {tag_list}", file=sys.stderr)
    sys.exit(1)


def get_commit_messages(tag: str, prev_tag: Optional[str] = None, from_tag: Optional[str] = None) -> str:
    """Get commit messages between tags."""
    if from_tag:
        log_range = f"{from_tag}..{tag}"
    elif prev_tag:
        log_range = f"{prev_tag}..{tag}"
    else:
        log_range = tag

    commits = run_git_command([
        'git', 'log', log_range,
        '--pretty=format:%s%n%b',
        '--no-merges',
        '--'  # prevent ambiguous argument error
    ])
    return commits


def generate_notes_with_gemini(
        commits: str,
        flavor: str,
        api_key: str,
        max_retries: int = 5
) -> str:
    """Generate release notes using Gemini API with exponential backoff."""

    system_prompt = (
        f"You are a professional technical writer and summarizer. "
        f"Transform raw commit messages into clean markdown release notes. "
        f"Flavor: **{flavor}**. "
        f"prod → user-focused only. staging/dev → allow internal notes. "
        f"Group under headers like '✨ New Features', '🐛 Bug Fixes', "
        f"'🧹 Improvements', '📚 Documentation', '🔧 Maintenance'. "
        f"Be concise and clear."
    )

    payload = {
        "contents": [
            {
                "role": "user",
                "parts": [
                    {"text": "Here are the raw commit messages to summarize into release notes:"},
                    {"text": commits}
                ]
            }
        ],
        "systemInstruction": {
            "parts": [{"text": system_prompt}]
        },
        "generationConfig": {
            "temperature": 0.7,
            "maxOutputTokens": 2048
        }
    }

    url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent"
    headers = {
        "Content-Type": "application/json",
        "x-goog-api-key": api_key
    }

    for attempt in range(1, max_retries + 1):
        try:
            response = requests.post(url, headers=headers, json=payload, timeout=60)

            if response.status_code == 200:
                data = response.json()
                if 'candidates' not in data or not data['candidates']:
                    print(f"⚠️  Empty response from Gemini API", file=sys.stderr)
                    print(f"Response: {data}", file=sys.stderr)
                    raise ValueError("Empty response from API")
                return data['candidates'][0]['content']['parts'][0]['text']

            elif response.status_code in (429, 500, 502, 503, 504):
                delay = 2 ** (attempt - 1)
                print(f"⚠️  Rate limit or server error (HTTP {response.status_code}). "
                      f"Retrying in {delay}s... ({attempt}/{max_retries})")
                if attempt < max_retries:
                    time.sleep(delay)
                continue

            else:
                print(f"❌ Gemini API call failed with status {response.status_code}", file=sys.stderr)
                print(f"Body: {response.text}", file=sys.stderr)
                sys.exit(1)

        except requests.exceptions.RequestException as e:
            print(f"❌ Request failed: {e}", file=sys.stderr)
            if attempt < max_retries:
                delay = 2 ** (attempt - 1)
                print(f"Retrying in {delay}s... ({attempt}/{max_retries})")
                time.sleep(delay)
                continue
            sys.exit(1)

    print("❌ Gemini API failed after all retries", file=sys.stderr)
    sys.exit(1)


def main():
    load_env_file()

    parser = argparse.ArgumentParser(
        description="Generate release notes from git commits using Gemini AI",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  %(prog)s v1.2.3                      # Generate prod notes for v1.2.3
  %(prog)s v1.2.3 --all-flavors        # Generate notes for all flavors
  %(prog)s v1.2.3 --flavor staging     # Generate staging notes
  %(prog)s v1.2.3 --from v1.2.0        # Compare specific range
  %(prog)s v1.2.3 -o notes.md          # Custom output file

Environment:
  GEMINI_API_KEY    Your Gemini API key (required)
        """
    )
    parser.add_argument('version', help='Version tag (e.g., v1.2.3 or 1.2.3)')
    parser.add_argument('--flavor', choices=['dev', 'staging', 'prod'], default='prod',
                        help='Deployment flavor (default: prod)')
    parser.add_argument('--api-key', help='Gemini API key (or set GEMINI_API_KEY env var or .env file)')
    parser.add_argument('--output', '-o', help='Output file path (default: release_notes/{version}.md)')
    parser.add_argument('--all-flavors', action='store_true',
                        help='Generate notes for all flavors (dev, staging, prod)')
    parser.add_argument('--from', dest='from_tag',
                        help='Start tag for comparison (overrides auto-detection)')

    args = parser.parse_args()

    api_key = args.api_key or os.environ.get('GEMINI_API_KEY')
    if not api_key:
        print("❌ Error: Gemini API key required", file=sys.stderr)
        print("\nSet it using one of these methods:", file=sys.stderr)
        print("  1. Environment variable: export GEMINI_API_KEY='your-key'", file=sys.stderr)
        print("  2. .env file: echo 'GEMINI_API_KEY=your-key' > .env", file=sys.stderr)
        print("  3. CLI argument: --api-key 'your-key'", file=sys.stderr)
        print("\nGet an API key at: https://aistudio.google.com/app/apikey", file=sys.stderr)
        sys.exit(1)

    # Normalize version tag
    version = args.version if args.version.startswith('v') else f"v{args.version}"

    # Check if we're in a git repository
    if subprocess.run(['git', 'rev-parse', '--git-dir'],
                      capture_output=True).returncode != 0:
        print("❌ Error: Not a git repository", file=sys.stderr)
        print("Run this script from inside a git repository", file=sys.stderr)
        sys.exit(1)

    # Validate version tag
    validate_tag(version)

    # Get commit range
    print(f"📋 Getting commits for {version}...")

    if args.from_tag:
        from_tag = args.from_tag if args.from_tag.startswith('v') else f"v{args.from_tag}"
        validate_tag(from_tag)
        prev_tag = from_tag
        print(f"   Comparing {from_tag}..{version}")
    else:
        prev_tag = get_previous_tag(version)
        from_tag = None
        if prev_tag:
            print(f"   Comparing {prev_tag}..{version}")
        else:
            print(f"   No previous tag found, using all commits up to {version}")

    commits = get_commit_messages(version, prev_tag, from_tag)

    if not commits.strip():
        print("⚠️  No commits found in range", file=sys.stderr)
        sys.exit(1)

    print(f"   Found {len(commits.splitlines())} commit lines")

    # Generate notes for requested flavor(s)
    flavors = ['dev', 'staging', 'prod'] if args.all_flavors else [args.flavor]

    for flavor in flavors:
        print(f"\n🧠 Generating release notes for {flavor}...")

        notes = generate_notes_with_gemini(commits, flavor, api_key)

        # Determine output path
        if args.output and not args.all_flavors:
            output_file = Path(args.output)
            output_file.parent.mkdir(parents=True, exist_ok=True)
        else:
            output_dir = Path("release_notes")
            output_dir.mkdir(parents=True, exist_ok=True)
            output_file = output_dir / f"{version}_{flavor}.md"

        # Write with UTF-8 to support emoji on all platforms (incl. Windows)
        output_file.write_text(notes, encoding="utf-8")
        print(f"✅ Generated release notes: {output_file}")

    print("\n✨ Done!")


if __name__ == '__main__':
    main()