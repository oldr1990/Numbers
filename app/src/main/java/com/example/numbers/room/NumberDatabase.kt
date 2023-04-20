package com.example.numbers.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.numbers.models.NumberItem

@Database(
    entities = [NumberItem::class],
    version = 1
)
@TypeConverters(NumberTypeConverter::class)
abstract class NumberDatabase : RoomDatabase() {
    abstract fun numberDao(): NumberDao
}