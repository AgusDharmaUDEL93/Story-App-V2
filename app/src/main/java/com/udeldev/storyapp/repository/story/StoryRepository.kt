package com.udeldev.storyapp.repository.story

import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.model.response.BasicResponse
import com.udeldev.storyapp.model.response.DetailStoryResponse
import com.udeldev.storyapp.model.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {

    suspend fun getAllStory(token : String) : Result<AllStoryResponse>
    suspend fun getDetailStory(token : String,id:String) : Result<DetailStoryResponse>

    suspend fun postMultiPart (token: String, file : MultipartBody.Part, description : RequestBody) : Result<BasicResponse>
}