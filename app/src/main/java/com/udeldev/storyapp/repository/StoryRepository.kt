package com.udeldev.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.udeldev.storyapp.helper.preference.TokenPreference
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.provider.config.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val pref: TokenPreference,
) {

    private val allStoryResult = MediatorLiveData<Result<AllStoryResponse>>()
    private val loginResult = MediatorLiveData<Result<LoginResponse>>()


    suspend fun saveSession(token: String) {
        pref.setToken(token)
    }

    fun getSession(): String {
        return runBlocking { pref.getToken().first() }
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getAllStory(): LiveData<Result<AllStoryResponse>> {
        allStoryResult.value = Result.Loading(true)
        val client = ApiConfig.getApiService().getAllStory("Bearer ${getSession()}")
        client.enqueue(
            object : Callback<AllStoryResponse> {
                override fun onResponse(call: Call<AllStoryResponse>, response: Response<AllStoryResponse>) {
                    if (response.isSuccessful) {
                        allStoryResult.value = Result.Success(response.body() as AllStoryResponse)
                        allStoryResult.value = Result.Loading(false)
                        return
                    }
                    allStoryResult.value = Result.Failure(response.message())
                    allStoryResult.value = Result.Loading(false)
                }

                override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                    allStoryResult.value = Result.Failure(t.toString())
                    allStoryResult.value = Result.Loading(false)
                }

            }
        )
        return allStoryResult
    }

    companion object {
        private var instance: StoryRepository? = null

        fun getInstance(
            pref: TokenPreference
        ): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(pref)
            }.also { instance = it }
        }
    }

}