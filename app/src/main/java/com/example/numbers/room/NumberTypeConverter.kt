package com.example.numbers.room

import androidx.room.TypeConverter
import com.example.numbers.models.NumberType

class NumberTypeConverter {
    @TypeConverter
    fun toNumberType(string: String): NumberType = NumberType.valueOf(string)

    @TypeConverter
    fun toNumberType(type: NumberType): String = type.name
}