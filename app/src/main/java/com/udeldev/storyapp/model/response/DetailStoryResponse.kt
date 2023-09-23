package com.udeldev.storyapp.model.response

import com.google.gson.annotations.SerializedName
import com.udeldev.storyapp.model.entity.ListStoryItem

data class DetailStoryResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val story: ListStoryItem? = null
)