package com.example.numbers.models

import com.squareup.moshi.Moshi

data class NumberItem(
    val number: Int,
    val description: String,
    val type: NumberType
) {
    val toJson: String get() = Moshi.Builder().build().adapter(NumberItem::class.java).toJson(this)
}
