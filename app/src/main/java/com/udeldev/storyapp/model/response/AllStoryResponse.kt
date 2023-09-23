package com.udeldev.storyapp.model.response

import com.google.gson.annotations.SerializedName
import com.udeldev.storyapp.model.entity.ListStoryItem

data class AllStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)