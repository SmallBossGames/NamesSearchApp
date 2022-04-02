package com.example.namessearchapp.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface INamesRepository {
    @ExperimentalCoroutinesApi
    val latestNamesList: Flow<GettingNamesResult>

    fun refreshNames()
    fun refreshNames(vararg patterns: String)
}

