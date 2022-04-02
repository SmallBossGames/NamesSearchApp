package com.example.namessearchapp

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

            delay(100)

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
}