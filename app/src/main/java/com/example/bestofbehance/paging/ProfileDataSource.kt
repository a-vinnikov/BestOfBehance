package com.example.bestofbehance.paging

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.dagger.NetworkModule
import com.example.bestofbehance.gson.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDataSource(val user: String, val context: Context) : PageKeyedDataSource<Int, CardBinding>() {

    val FIRST_PAGE = 1

    private val recList: MutableList<CardBinding> = mutableListOf()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CardBinding>) {
        NetworkModule().providesBehanceApi(NetworkModule().providesRetrofit(NetworkModule().providesOkHttpClient(context).build())).getUserProjects(user, FIRST_PAGE).enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {}

            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                response.body()?.run {
                    projects(user,this){ result ->
                        val abc = result.sortedByDescending { it.published }
                        if (abc.size < 48){
                            callback.onResult(abc, null, FIRST_PAGE)
                        } else {
                            callback.onResult(abc, null, FIRST_PAGE + 1)
                        }
                        }
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        if(params.key != 1){
            NetworkModule().providesBehanceApi(NetworkModule().providesRetrofit(NetworkModule().providesOkHttpClient(context).build())).getUserProjects(user, params.key)
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
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        NetworkModule().providesBehanceApi(NetworkModule().providesRetrofit(NetworkModule().providesOkHttpClient(context).build())).getUserProjects(user, params.key)
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
            val thumbnail = listResponse[i]?.covers?.jsonMember115
            val artistName = listResponse[i]?.owners?.get(0)?.displayName
            val avatar = listResponse[i]?.owners?.get(0)?.images?.jsonMember138
            val appreciations = listResponse[i]?.stats?.appreciations
            val views = listResponse[i]?.stats?.views
            val comments = listResponse[i]?.stats?.comments
            val artName = listResponse[i]?.name
            val id = listResponse[i]?.id
            val published = listResponse[i]?.publishedOn
            val url = listResponse[i]?.url

            recList.add(
                CardBinding(
                    id!!,
                    art,
                    thumbnail,
                    avatar,
                    artistName,
                    artName,
                    views.toString(),
                    appreciations.toString(),
                    comments.toString(),
                    username,
                    published,
                    url
                )
            )
            i + 1
        }
        myCallBack.invoke(recList)
    }
}