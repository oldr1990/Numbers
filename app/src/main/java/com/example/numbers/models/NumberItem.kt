package com.example.numbers.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Moshi

@Entity(tableName = "numbers")
data class NumberItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val number: String,
    val description: String,
    val type: NumberType
) {
    val toJson: String get() = Moshi.Builder().build().adapter(NumberItem::class.java).toJson(this)
}
