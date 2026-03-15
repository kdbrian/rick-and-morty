import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devToolsKsp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.kdbrian.rickmorty"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.kdbrian.rickmorty"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    flavorDimensions += "environment"

    productFlavors {

        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }

        create("beta") {
            dimension = "environment"
            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta"

            buildConfigField("String", "ENVIRONMENT", "\"beta\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }

        create("betaFeature") {
            dimension = "environment"
            applicationIdSuffix = ".beta.feature"
            versionNameSuffix = "-beta-feature"

            buildConfigField("String", "ENVIRONMENT", "\"beta-feature\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }

        create("live") {
            dimension = "environment"

            // No suffixes for production
            buildConfigField("String", "ENVIRONMENT", "\"live\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "false")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.compose.material.icons.extended.android)
    implementation(libs.squareup.retrofit2)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.squareup.retrofit2.converter.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.palette.ktx)
    implementation("androidx.compose.material:material:1.10.4")

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // google fonts
    implementation(libs.ui.text.google.fonts)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}