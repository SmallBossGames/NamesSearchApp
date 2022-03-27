package com.example.namessearchapp.data

import com.example.namessearchapp.demo.demoNames
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NamesDemoRepository(
    private val demoNamesList: List<String> = demoNames,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val names get() = flow {
        delay(demoRequestDelayInMillis)
        emit(demoNamesList)
    }

    private val currentNamesMutable = MutableSharedFlow<Flow<List<String>>>()

    var demoRequestDelayInMillis: Long = 1000

    @ExperimentalCoroutinesApi
    val latestNamesList = channelFlow {
        currentNamesMutable.collectLatest { flow ->
            flow.collectLatest { list ->
                send(list)
            }
        }
    }

    fun refreshNames() {
        coroutineScope.launch {
            currentNamesMutable.emit(names)
        }
    }

    fun refreshNames(substring: String) {
        coroutineScope.launch {
            currentNamesMutable.emit(
                names.map { names -> names.filter { it.contains(substring) } }
            )
        }
    }
}