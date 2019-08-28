package com.example.bestofbehance.viewModels

import com.example.bestofbehance.retrofit.BehanceApiInterface
import com.example.bestofbehance.binding.*
import com.example.bestofbehance.gson.*
import com.example.bestofbehance.fragments.MultiList
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val iListCon: MutableList<MultiList> = mutableListOf()
    private val iListCom: MutableList<MultiList> = mutableListOf()
    private val userList: MutableList<ProfileBinding> = mutableListOf()
    private val apiKey = "xMrW480v8SrR9J02koQXiIEEMr3uzIfd"
    //xMrW480v8SrR9J02koQXiIEEMr3uzIfd
    //0QmPh684DRz1SpWHDikkyFCzLShGiHPi

    fun generalRetrofit(page: Int, myCallBack: (result: MutableList<CardBinding>) -> Unit){

        val call = service()?.getGeneral("appreciations", page, apiKey)

        call?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                val listResponse = response.body()?.projects


                for (i in listResponse!!.indices) {

                    val art = listResponse[i]?.covers?.original
                    val artistName = listResponse[i]?.owners?.get(0)?.displayName
                    val avatar = listResponse[i]?.owners?.get(0)?.images?.jsonMember138
                    val appreciations = listResponse[i]?.stats?.appreciations
                    val views = listResponse[i]?.stats?.views
                    val comments = listResponse[i]?.stats?.comments
                    val artName = listResponse[i]?.name
                    val id = listResponse[i]?.id
                    val username = listResponse[i]?.owners?.get(0)?.username
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

        })
    }


    fun projectRetrofit(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit){

        val call = service()?.getProject(projectId.toString(), apiKey)

        call?.enqueue(object : Callback<ImageResponse> {
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ImageResponse>, response: retrofit2.Response<ImageResponse>) {

                val listResponse = response.body()?.project?.modules

                for (i in listResponse!!.indices) {

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

        val call = service()?.getComments(projectId.toString(), apiKey)

        call?.enqueue(object : Callback<CommentsMain> {
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
                    val commentatorName = listResponse?.get(i)?.user?.displayName
                    val commentatorUsername = listResponse?.get(i)?.user?.username
                    val comment = listResponse?.get(i)?.comment
                    val date = listResponse?.get(i)?.createdOn.toString()

                    if (i == 0) {
                        iListCom.add(MultiList.CountList(CountBinding(numberOfComments)))
                    }
                    iListCom.add(MultiList.CommentsList(CommentsBinding(commentsAvatarView, commentatorName, commentatorUsername, comment, date)))
                    i + 1
                }
                myCallBack.invoke(iListCom)
            }

        })

    }

    fun userRetrofit(username: String, myCallBack: (result: MutableList<ProfileBinding>) -> Unit){

        val call = service()?.getUser(username, apiKey)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<UserResponse>, response: retrofit2.Response<UserResponse>) {
                val listResponse = response.body()?.user

                val id = listResponse?.id
                val avatar = listResponse?.images?.jsonMember276
                val artistName = listResponse?.displayName
                val cityCountry = listResponse?.city + ", " + listResponse?.country
                val views = listResponse?.stats?.views
                val appreciations = listResponse?.stats?.appreciations
                val followers = listResponse?.stats?.followers
                val following = listResponse?.stats?.following
                var aboutArtist = listResponse?.sections?.aboutMe
                val check = listResponse?.fields?.size!!
                lateinit var post: String
                val sb = StringBuilder()
                post = if (check == 0){
                    "Nothing here"
                } else{
                    (listResponse.fields.indices).forEach { i ->
                        if (i == listResponse.fields.size - 1){
                            sb.append(listResponse.fields[i])
                        } else {
                            sb.append(listResponse.fields[i] + ", ")
                        }
                    }
                    sb.toString()
                }



                if (aboutArtist == null) aboutArtist = "No information"

                userList.add(ProfileBinding(id!!, avatar, artistName, cityCountry, views.toString(), appreciations.toString(), followers.toString(), following.toString(), aboutArtist, post))

                myCallBack.invoke(userList)
            }
        })

    }

    fun userProjectsRetrofit(username: String, page: Int, myCallBack: (result: MutableList<CardBinding>) -> Unit){

        val call = service()?.getUserProjects(username, page, apiKey)

        call?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<GeneralResponse>, response: retrofit2.Response<GeneralResponse>) {
                val listResponse = response.body()?.projects


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

        })
    }

    private fun service(): BehanceApiInterface? {
        val client = Retrofit.Builder()
            .baseUrl("https://api.behance.net/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return client.create(BehanceApiInterface::class.java)
    }

}