package com.udeldev.storyapp.repository.token

interface TokenRepository {
    suspend fun saveSession(token: String)
    fun getSession() :String
    suspend fun logout()
}