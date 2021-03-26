package com.example.breakingbad.data.network

open class Response<T> {
    class Success<T>(val result: T): Response<T>()
    class Failure<T>(val message: String? = null): Response<T>()
}

// MESSAGES
val NO_INTERNET = "nointernet"