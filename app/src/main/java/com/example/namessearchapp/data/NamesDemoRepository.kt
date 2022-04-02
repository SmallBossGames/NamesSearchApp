package com.example.namessearchapp.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NamesDemoRepository(
    private val demoNamesList: List<String>,
) : INamesRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val names get() = flow {
        delay(demoRequestDelayInMillis)
        emit(demoNamesList)
    }

    private val currentNamesMutable = MutableStateFlow(names)

    var demoRequestDelayInMillis: Long = 1000

    @ExperimentalCoroutinesApi
    override val latestNamesList = channelFlow {
        currentNamesMutable.collectLatest { flow ->
            flow.collectLatest { list ->
                send(list)
            }
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
}