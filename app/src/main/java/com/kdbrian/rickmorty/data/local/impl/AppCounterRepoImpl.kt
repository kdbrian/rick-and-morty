package com.kdbrian.rickmorty.data.local.impl

import android.content.Context
import com.kdbrian.rickmorty.data.local.dao.AppCounterDao
import com.kdbrian.rickmorty.domain.model.AppCounter
import com.kdbrian.rickmorty.domain.repo.AppCounterRepo
import com.kdbrian.rickmorty.util.dispatchIO
import com.kdbrian.rickmorty.util.dispatchIOResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppCounterRepoImpl @Inject constructor(
    private val dao: AppCounterDao
) : AppCounterRepo {
    override fun counter(): Flow<List<AppCounter>> = dao.counter()

    override suspend fun count(appCounter: AppCounter): Result<Boolean> = dispatchIOResult {
        dao.update(appCounter)
        true
    }
}