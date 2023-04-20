package com.example.numbers.screens.home

import com.example.numbers.models.NumberItem
import com.example.numbers.models.NumberType
import com.example.numbers.network.NumberApi
import com.example.numbers.network.Response
import com.example.numbers.room.NumberDao
import javax.inject.Inject

class NumberRepository @Inject constructor(
    private val dao: NumberDao,
    private val numberApi: NumberApi
) {
    suspend fun searchNumber(
        number: String,
        type: NumberType,
        isRandom: Boolean
    ): Response<NumberItem> {
        return try {
            val result =
                numberApi.getNumber(if (isRandom) "random" else number, type.name.lowercase())
            if (result.isEmpty()) {
                Response.Error("")
            } else {
                val newNumber =
                    if (isRandom) result.split(" ").first { it.toIntOrNull() != null } else number
                val item = NumberItem(0, newNumber, result, type)
                dao.upsertNumber(item)
                Response.Success(item)
            }
        } catch (e: java.lang.Exception) {
            Response.Error(e.message.orEmpty())
        }
    }

    suspend fun getNumbersFromDB() = dao.getAllNumbers().sortedByDescending { it.id }
}