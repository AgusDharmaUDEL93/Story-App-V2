package com.udeldev.storyapp.repository.auth

import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.provider.config.ApiConfig
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl : AuthRepository {
    override suspend fun loginUser(email: String, password: String): Result<LoginResponse> {
        return suspendCoroutine { continuation ->
            var loginResponse: Result<LoginResponse>
            val client = ApiConfig.getApiService().loginAuth(email, password)
            client.enqueue(
                object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            loginResponse = Result.Success(response.body() as LoginResponse)
                            continuation.resume(loginResponse)
                            return
                        }
                        loginResponse = Result.Failure(response.message())
                        continuation.resume(loginResponse)
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        loginResponse = Result.Failure(t.toString())
                        continuation.resume(loginResponse)
                    }

                }
            )

        }
    }

}