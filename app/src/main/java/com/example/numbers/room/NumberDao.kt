package com.example.numbers.room


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.numbers.models.NumberItem

@Dao
interface NumberDao {
 @Upsert
 suspend fun upsertNumber(number: NumberItem)

 @Query("SELECT * FROM numbers")
 suspend fun getAllNumbers(): List<NumberItem>
}