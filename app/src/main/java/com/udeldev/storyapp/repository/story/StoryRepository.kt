package com.udeldev.storyapp.repository.story

import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.model.response.DetailStoryResponse
import com.udeldev.storyapp.model.response.LoginResponse

interface StoryRepository {

    suspend fun getAllStory(token : String) : Result<AllStoryResponse>
}