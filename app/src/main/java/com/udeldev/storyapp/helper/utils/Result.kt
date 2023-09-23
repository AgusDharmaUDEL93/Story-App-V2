package com.udeldev.storyapp.helper.utils

sealed class Result<out T> {
    data class Loading<out T>(val state: Boolean) : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure<out T>(val throwable: String) : Result<T>()
}