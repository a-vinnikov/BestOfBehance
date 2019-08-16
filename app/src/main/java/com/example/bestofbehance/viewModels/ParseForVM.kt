package com.example.bestofbehance.viewModels

import com.example.bestofbehance.Retrofit.BehanceApiInterface
import com.example.bestofbehance.binding.CardBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding
import com.example.bestofbehance.gson.*
import com.example.bestofbehance.layout.MultiList
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val iListCon: MutableList<MultiList> = mutableListOf()
    private val iListCom: MutableList<MultiList> = mutableListOf()
    private val apiKey = "0QmPh684DRz1SpWHDikkyFCzLShGiHPi"

    fun generalRetrofit(page: Int, myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val client = Retrofit.Builder()
            .baseUrl("https://api.behance.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = client.create(BehanceApiInterface::class.java)
        val call = service.getGeneral("appreciations", page, apiKey)

        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                val listResponse = response.body()?.projects


                for (i in 0 until listResponse!!.size) {

                    val arts = listResponse[i]?.covers?.original
                    val names = listResponse[i]?.owners?.get(0)?.displayName
                    val avatars = listResponse[i]?.owners?.get(0)?.images?.jsonMember138
                    val appreciations = listResponse[i]?.stats?.appreciations
                    val views = listResponse[i]?.stats?.views
                    val comments = listResponse[i]?.stats?.comments
                    val posts = listResponse[i]?.fields?.get(0)
                    val id = listResponse[i]?.id
                    val username = listResponse[i]?.owners?.get(0)?.username

                    recList.add(
                        CardBinding(
                            id!!,
                            arts,
                            avatars,
                            names,
                            posts,
                            views.toString(),
                            appreciations.toString(),
                            comments.toString(),
                            username

                        )
                    )
                    i + 1
                }
                myCallBack.invoke(recList)
            }

        })
    }


    fun projectRetrofit(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit){

        val client = Retrofit.Builder()
            .baseUrl("https://api.behance.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = client.create(BehanceApiInterface::class.java)
        val call = service.getProject(projectId.toString(), apiKey)

        call.enqueue(object : Callback<ImageResponse> {
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ImageResponse>, response: retrofit2.Response<ImageResponse>) {

                val listResponse = response.body()?.project?.modules

                for (i in 0 until listResponse!!.size) {

                    when (listResponse[i]!!.type) {
                        "image" -> {val image = listResponse[i]?.src
                            iListCon.add(MultiList.ImageList(ImageBinding(image)))}
                        "text" -> {val text = listResponse[i]?.text.toString()
                            val text1 = Jsoup.parse(text).text()
                            iListCon.add(MultiList.TextList(TextBinding(text1)))}
                        "media_collection" -> {
                            for (j in 0 until listResponse[i]?.components!!.size){
                                val collectionItem = listResponse[i]?.components!![j]?.src
                                iListCon.add(MultiList.ImageList(ImageBinding(collectionItem)))} } }
                    i + 1
                }
                myCallBack.invoke(iListCon)
            }
        })
    }

    fun commentsRetrofit(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit){

        val client = Retrofit.Builder()
            .baseUrl("https://api.behance.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = client.create(BehanceApiInterface::class.java)
        val call = service.getComments(projectId.toString(), apiKey)

        call.enqueue(object : Callback<CommentsMain> {
            override fun onFailure(call: Call<CommentsMain>, t: Throwable) {

            }

            override fun onResponse(call: Call<CommentsMain>, response: retrofit2.Response<CommentsMain>) {

                val listResponse = response.body()?.comments
                val numberOfComments = response.body()?.comments?.size

                if(numberOfComments == 0){
                    iListCom.add(MultiList.CountList(CountBinding(numberOfComments.toInt())))
                }
                for (i in 0 until numberOfComments!!) {

                    val commentsAvatarView = listResponse?.get(i)?.user?.images?.jsonMember138
                    val commentsName = listResponse?.get(i)?.user?.displayName
                    val comment = listResponse?.get(i)?.comment
                    val date = listResponse?.get(i)?.createdOn.toString()


                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, id))
                    if (i == 0) {
                        iListCom.add(MultiList.CountList(CountBinding(numberOfComments)))
                    }
                    iListCom.add(MultiList.CommentsList(CommentsBinding(commentsAvatarView, commentsName, comment, date)))
                    i + 1
                }
                myCallBack.invoke(iListCom)
            }

        })

    }

}