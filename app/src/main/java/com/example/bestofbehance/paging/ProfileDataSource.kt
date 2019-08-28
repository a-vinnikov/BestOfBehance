package com.example.bestofbehance.paging

import androidx.paging.PageKeyedDataSource
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.gson.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataSource(val user: String) : PageKeyedDataSource<Int, CardBinding>() {

    val FIRST_PAGE = 1
    val API_KEY = "xMrW480v8SrR9J02koQXiIEEMr3uzIfd"

    private val recList: MutableList<CardBinding> = mutableListOf()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CardBinding>) {
        RetrofitClient.getInstance().getApi().getUserProjects(user, FIRST_PAGE, API_KEY).enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {}

            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                response.body()?.run {
                    projects(user,this){ result ->
                        val abc = result.sortedByDescending { it.published }
                        callback.onResult(abc, null, FIRST_PAGE + 1)}
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        RetrofitClient.getInstance().getApi().getUserProjects(user, params.key, API_KEY)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    response.body()?.run {
                        val key = params.key + 1
                        response.body()?.run {
                            projects(user,this){ result ->
                                val abc = result.sortedByDescending { it.published }
                                callback.onResult(abc, key)}
                        }
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        RetrofitClient.getInstance().getApi().getUserProjects(user, params.key, API_KEY)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    response.body()?.run {
                        val key = if (params.key > 1) params.key - 1 else null
                        projects(user,this){ result ->
                            val abc = result.sortedByDescending { it.published }
                            callback.onResult(abc, key)}
                    }
                }
            })
    }

    private fun projects(username: String, response: GeneralResponse, myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val listResponse = response.projects


        for (i in listResponse!!.indices) {

            val art = listResponse[i]?.covers?.original
            val artistName = listResponse[i]?.owners?.get(0)?.displayName
            val avatar = listResponse[i]?.owners?.get(0)?.images?.jsonMember138
            val appreciations = listResponse[i]?.stats?.appreciations
            val views = listResponse[i]?.stats?.views
            val comments = listResponse[i]?.stats?.comments
            val artName = listResponse[i]?.name
            val id = listResponse[i]?.id
            val published = listResponse[i]?.publishedOn

            recList.add(
                CardBinding(
                    id!!,
                    art,
                    avatar,
                    artistName,
                    artName,
                    views.toString(),
                    appreciations.toString(),
                    comments.toString(),
                    username,
                    published
                )
            )
            i + 1
        }
        myCallBack.invoke(recList)
    }
}