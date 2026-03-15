@file:OptIn(FlowPreview::class)

package com.kdbrian.rickmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.rickmorty.domain.dto.AppCounterDto
import com.kdbrian.rickmorty.domain.dto.toCounter
import com.kdbrian.rickmorty.domain.repo.AppCounterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val appCounterRepo: AppCounterRepo
) : ViewModel() {


    val counter = appCounterRepo.counter()
//        .debounce(3_000)

    fun count(dto: AppCounterDto) {
        viewModelScope.launch {
            appCounterRepo.count(dto.toCounter())
        }
    }

}