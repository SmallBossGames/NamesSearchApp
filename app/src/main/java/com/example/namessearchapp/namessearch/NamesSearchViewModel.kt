package com.example.namessearchapp.namessearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.namessearchapp.data.ITestNamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class NamesSearchViewModel @Inject constructor(
    private val namesRepository: ITestNamesRepository
): ViewModel() {
    var namePattern by mutableStateOf("")

    @ExperimentalCoroutinesApi
    val namesList = namesRepository.latestNamesList.asLiveData()

    fun onNamePatternChanged(pattern: String){
        namePattern = pattern

        when(pattern){
            "" -> namesRepository.refreshNames()
            else -> namesRepository.refreshNames(*pattern.split(" ").toTypedArray())
        }
    }

    fun onEmulateError(){
        namesRepository.emulateError()
    }
}