package com.example.bestofbehance.Retrofit

import com.example.bestofbehance.gson.CommentsMain
import com.example.bestofbehance.gson.GeneralResponse
import com.example.bestofbehance.gson.ImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BehanceApiInterface {

    @GET("projects/{projectId}")
    fun getProject(@Path("projectId") projectId: String, @Query("api_key") api_key: String): Call<ImageResponse>

    @GET("projects/{projectId}/comments")
    fun getComments(@Path("projectId") projectId: String, @Query("api_key") api_key: String): Call<CommentsMain>

    @GET("projects")
    fun getGeneral(@Query("sort") sort: String, @Query("page") page: Int, @Query("api_key") api_key: String): Call<GeneralResponse>

}