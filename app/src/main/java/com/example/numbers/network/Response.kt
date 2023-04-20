package com.example.numbers.network

sealed interface Response<T>{
    class Success<T>(val data: T):Response<T>
    class Error<T>(val message: String): Response<T>
}