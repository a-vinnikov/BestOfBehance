package com.example.bestofbehance.paging
import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.dagger.NetworkModule
import com.example.bestofbehance.databases.CardDataBase
import com.example.bestofbehance.gson.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

const val FIRST_PAGE = 1

class BestDataSource(val context: Context) : PageKeyedDataSource<Int, CardBinding>() {
    private val recList: MutableList<CardBinding> = mutableListOf()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CardBinding>) {
        with(NetworkModule(context)){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getGeneral("appreciations", FIRST_PAGE)}.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {}

            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                response.body()?.run {
                    general(this){result ->
                        dbFill(result)
                        val abc = result.sortedByDescending { it.published }.toMutableList()
                        callback.onResult(abc, null, FIRST_PAGE + 1)}
                        Timber.d("Loaded first page")
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        with(NetworkModule(context)){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getGeneral("appreciations", params.key)}
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    response.body()?.run {
                        val key = params.key + 1
                        response.body()?.run {
                            general(this){result ->
                                val abc = result.sortedByDescending { it.published }
                                callback.onResult(abc, key)}
                            Timber.d("Loaded next page, ${params.key} page")
                        }
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CardBinding>) {
        with(NetworkModule(context)){providesBehanceApi(providesRetrofit(providesOkHttpClient().build())).getGeneral("appreciations", params.key)}
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    response.body()?.run {
                        val key = if (params.key > 1) params.key - 1 else null
                        general(this){result ->
                            val abc = result.sortedByDescending { it.published }
                            callback.onResult(abc, key)}
                        Timber.d("Loaded previous page, ${params.key} page")
                    }
                }
            })
    }

    fun general(response: GeneralResponse, myCallBack: (result: MutableList<CardBinding>) -> Unit){
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
            val username = listResponse[i]?.owners?.get(0)?.username
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

    private fun dbFill(list: MutableList<CardBinding>){
        CardDataBase.getDatabase(context)?.getCardDao()?.deleteAll()
        for (i in 0 until list.size) {
            CardDataBase.getDatabase(context)?.getCardDao()?.insert(list[i])
        }
        Timber.d("DB updated")
    }
}