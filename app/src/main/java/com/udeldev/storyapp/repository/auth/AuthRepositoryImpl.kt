package com.udeldev.storyapp.repository.auth

import android.util.Log
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.BasicResponse
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.provider.config.ApiConfig
import com.udeldev.storyapp.repository.story.StoryRepositoryImpl
import org.json.JSONObject
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
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        loginResponse = Result.Failure(jsonObj.getString("message") ?: response.message())
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

    override suspend fun registerUser(name: String, email: String, password: String): Result<BasicResponse> {
        return suspendCoroutine { continuation ->
            var registerResponse : Result<BasicResponse>
            val client = ApiConfig.getApiService().registerAuth(name, email, password)
            client.enqueue(
                object : Callback<BasicResponse>{
                    override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                        if (response.isSuccessful){
                            registerResponse= Result.Success(response.body() as BasicResponse)
                            continuation.resume(registerResponse)
                            return
                        }
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        registerResponse = Result.Failure(jsonObj.getString("message") ?: response.message())
                        continuation.resume(registerResponse)
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        registerResponse = Result.Failure(t.toString())
                        continuation.resume(registerResponse)
                    }

                }
            )

        }
    }
    companion object{
        private const val TAG = "AuthRepository"
    }


}