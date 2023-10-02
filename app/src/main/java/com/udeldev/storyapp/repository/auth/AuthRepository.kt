package com.udeldev.storyapp.repository.auth

import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.LoginResponse

interface AuthRepository {
    suspend fun loginUser(email:String, password :String) : Result<LoginResponse>

}