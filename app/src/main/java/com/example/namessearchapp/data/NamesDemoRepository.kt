package com.example.namessearchapp.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.IllegalStateException

class NamesDemoRepository(
    private val demoNamesList: List<String>,
) : ITestNamesRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val names get() = flow {
        delay(demoRequestDelayInMillis)
        emit(demoNamesList)
    }

    private val currentNamesMutable = MutableStateFlow(names)

    var demoRequestDelayInMillis: Long = 1000

    @ExperimentalCoroutinesApi
    override val latestNamesList = currentNamesMutable
        .mapLatest {
            try {
                Success(it.single())
            } catch (e: IllegalArgumentException){
                Error
            }
        }

    override fun refreshNames() {
        coroutineScope.launch {
            currentNamesMutable.emit(names)
        }
    }

    override fun refreshNames(vararg patterns: String) {
        coroutineScope.launch {
            currentNamesMutable.emit(
                names.map { names ->
                    names.filter { name ->
                        patterns.all { name.contains(it, true) }
                    }
                }
            )
        }
    }

    override fun emulateError() {
        coroutineScope.launch {
            val errorFlow = flow<List<String>> {
                throw IllegalArgumentException()
            }

            currentNamesMutable.emit(errorFlow)
        }
    }
}

