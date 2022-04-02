package com.example.namessearchapp

import com.example.namessearchapp.data.Error
import com.example.namessearchapp.data.GettingNamesResult
import com.example.namessearchapp.data.NamesDemoRepository
import com.example.namessearchapp.data.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import org.junit.Test

class NamesDemoRepositoryTests {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Test
    @ExperimentalCoroutinesApi
    fun refreshNamesInConcurrency_shouldTakeLastValue() {
        // Arrange
        val repository = NamesDemoRepository(listOf("A", "B"))

        var result = emptyList<String>()

        // Act
        runBlocking {
            val job = coroutineScope.launch {
                repository.latestNamesList.take(1).collect { result = (it as Success).names }
            }

            delay(10)

            repository.demoRequestDelayInMillis = 300
            repository.refreshNames("A")

            delay(100)

            repository.demoRequestDelayInMillis = 1000
            repository.refreshNames("B")

            job.join()
        }

        // Assert
        assertThat(result).isEqualTo(listOf("B"))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun emulateError_shouldReturnErrorResult() {
        // Arrange
        val repository = NamesDemoRepository(listOf("A", "B"))

        var result: GettingNamesResult? = null

        // Act
        runBlocking {
            val job = coroutineScope.launch {
                repository.latestNamesList.take(1).collect {
                    result = it
                }
            }

            delay(10)

            repository.emulateError()

            job.join()
        }

        // Assert
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun refreshNames_shouldReturnSuccessWithAllNames() {
        // Arrange
        val expectedNamesList = listOf("A", "B")

        val repository = NamesDemoRepository(expectedNamesList)

        var result: GettingNamesResult? = null

        // Act
        runBlocking {
            val job = coroutineScope.launch {
                repository.latestNamesList.take(1).collect {
                    result = it
                }
            }

            delay(10)

            repository.refreshNames()

            job.join()
        }

        // Assert
        assertThat(result).isInstanceOf(Success::class.java)
        assertThat((result as Success).names).isEqualTo(expectedNamesList)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun refreshNamesWithPattern_shouldReturnSuccessWithAllNames() {
        // Arrange
        val expectedNamesList = listOf("FirstName1 SecondName1", "FirstName2 SecondName2")

        val repository = NamesDemoRepository(expectedNamesList)

        var result: GettingNamesResult? = null

        // Act
        runBlocking {
            val job = coroutineScope.launch {
                repository.latestNamesList.take(1).collect {
                    result = it
                }
            }

            delay(10)

            repository.refreshNames("FirstName1", "SecondName1")

            job.join()
        }

        // Assert
        assertThat(result).isInstanceOf(Success::class.java)
        assertThat((result as Success).names).isEqualTo(listOf("FirstName1 SecondName1"))
    }
}