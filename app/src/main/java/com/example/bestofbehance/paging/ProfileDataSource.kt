package com.example.bestofbehance.paging

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.example.bestofbehance.R
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.dagger.module.NetworkModule
import com.example.bestofbehance.gson.GeneralResponse

class ProfileDataSource(val user: String, val context: Context) : PageKeyedDataSource<Int, CardBinding>() {

    private val recList: MutableList<CardBinding> = mutableListOf()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CardBinding>) {
        with(NetworkModule()){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getUserProjects(user, FIRST_PAGE)}.run { generalResponse, _ ->
            generalResponse?.run {
                projects(user,this){ result ->
                    val firstResponse = result.sortedByDescending { it.published }
                    if (firstResponse.size < context.resources.getInteger(R.integer.responseSize)){
                        callback.onResult(firstResponse, null, FIRST_PAGE)
                    } else {
                        callback.onResult(firstResponse, null, FIRST_PAGE + 1)
                    }
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        if(params.key != 1){

            with(NetworkModule()){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getUserProjects(user, params.key)}.run { generalResponse, _ ->
                val key = params.key + 1
                generalResponse?.run {
                    projects(user,this){ result ->
                        val anotherResponse = result.sortedByDescending { it.published }
                        callback.onResult(anotherResponse, key)}
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {

        with(NetworkModule()){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getUserProjects(user, params.key)}.run { generalResponse, _ ->
            generalResponse?.run {
                val key = if (params.key > 1) params.key - 1 else null
                projects(user,this){ result ->
                    val abc = result.sortedByDescending { it.published }
                    callback.onResult(abc, key)}
            }
        }
    }

    private fun projects(username: String, response: GeneralResponse, myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val listResponse = response.projects
        recList.clear()

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