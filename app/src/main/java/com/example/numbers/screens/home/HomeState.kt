package com.example.numbers.screens.home

import com.example.numbers.models.NumberItem
import com.example.numbers.models.NumberType

data class HomeState(
    val isLoading: Boolean = false,
    val search: String = "",
    val type: NumberType = NumberType.TRIVIA,
    val list: List<NumberItem> = emptyList()
)