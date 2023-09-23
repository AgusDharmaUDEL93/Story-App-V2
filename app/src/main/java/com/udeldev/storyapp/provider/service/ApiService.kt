package com.udeldev.storyapp.provider.service

import com.udeldev.storyapp.model.response.AllStoryResponse
import com.udeldev.storyapp.model.response.BasicResponse
import com.udeldev.storyapp.model.response.DetailStoryResponse
import com.udeldev.storyapp.model.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerAuth(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginAuth(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun postStoriesData(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<BasicResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
    ): Call<AllStoryResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<DetailStoryResponse>
}