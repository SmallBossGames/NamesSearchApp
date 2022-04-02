package com.example.namessearchapp.data

sealed interface GettingNamesResult
object Error : GettingNamesResult
class Success(val names: List<String>): GettingNamesResult