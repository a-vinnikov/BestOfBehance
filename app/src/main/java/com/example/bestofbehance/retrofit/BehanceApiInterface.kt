package com.example.bestofbehance.retrofit

import com.example.bestofbehance.gson.CommentsMain
import com.example.bestofbehance.gson.GeneralResponse
import com.example.bestofbehance.gson.ImageResponse
import com.example.bestofbehance.gson.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface BehanceApiInterface {

    @GET("projects/{projectId}")
    fun getProject(@Path("projectId") projectId: String): Call<ImageResponse>

    @GET("projects/{projectId}/comments")
    fun getComments(@Path("projectId") projectId: String): Call<CommentsMain>

    @GET("projects")
    fun getGeneral(@Query("sort") sort: String, @Query("page") page: Int): Call<GeneralResponse>

    @GET("users/{user}")
    fun getUser(@Path("user") user: String): Call<UserResponse>

    @GET("users/{user}/projects")
    fun getUserProjects(@Path("user") user: String, @Query("page") page: Int): Call<GeneralResponse>

}