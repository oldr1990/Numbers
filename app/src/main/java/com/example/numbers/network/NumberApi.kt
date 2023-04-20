package com.example.numbers.network


import retrofit2.http.GET
import retrofit2.http.Path

interface NumberApi {
    @GET("/{number}/{type}")
    suspend fun getNumber(@Path("number") number: String, @Path("type") type: String): String
}