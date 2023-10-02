package com.udeldev.storyapp.repository.story

import com.udeldev.storyapp.helper.preference.TokenPreference
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.model.response.LoginResponse
import com.udeldev.storyapp.provider.config.ApiConfig
import com.udeldev.storyapp.provider.service.ApiService
import com.udeldev.storyapp.repository.token.TokenRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StoryRepositoryImpl : StoryRepository {

    override suspend fun getAllStory(token: String): Result<AllStoryResponse> {
        return suspendCoroutine { continuation ->
            var allStoryResult: Result<AllStoryResponse>
            val client = ApiConfig.getApiService().getAllStory("Bearer $token")
            client.enqueue(
                object : Callback<AllStoryResponse> {
                    override fun onResponse(call: Call<AllStoryResponse>, response: Response<AllStoryResponse>) {
                        if (response.isSuccessful) {
                            allStoryResult = Result.Success(response.body() as AllStoryResponse)
                            continuation.resume(allStoryResult)
                            return
                        }
                        allStoryResult = Result.Failure(response.message())
                        continuation.resume(allStoryResult)
                    }

                    override fun onFailure(call: Call<AllStoryResponse>, t: Throwable) {
                        allStoryResult = Result.Failure(t.toString())
                        continuation.resume(allStoryResult)

                    }

                }
            )
        }
    }


}