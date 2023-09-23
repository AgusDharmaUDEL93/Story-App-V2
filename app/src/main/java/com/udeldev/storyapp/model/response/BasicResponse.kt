package com.udeldev.storyapp.model.response

import com.google.gson.annotations.SerializedName

data class BasicResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
