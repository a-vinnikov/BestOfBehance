package com.example.bestofbehance.viewModels

import androidx.core.text.HtmlCompat
import com.example.bestofbehance.retrofit.BehanceApiInterface
import com.example.bestofbehance.binding.*
import com.example.bestofbehance.dagger.NetworkModule
import com.example.bestofbehance.gson.*
import com.example.bestofbehance.classesToSupport.MultiList
import retrofit2.Call
import retrofit2.Callback
import java.lang.StringBuilder

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val iListCon: MutableList<MultiList> = mutableListOf()
    private val iListCom: MutableList<MultiList> = mutableListOf()
    private val userList: MutableList<ProfileBinding> = mutableListOf()
    private val linksList: MutableMap<String, String?> = mutableMapOf()


    fun projectRetrofit(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit) {

        val call = service()?.getProject(projectId.toString())

        call?.enqueue(object : Callback<ImageResponse> {
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ImageResponse>,
                response: retrofit2.Response<ImageResponse>
            ) {

                val listResponse = response.body()?.project?.modules

                if (listResponse != null) for (i in listResponse.indices) {

                    when (listResponse[i]!!.type) {
                        "image" -> {
                            val image = listResponse[i]?.src
                            iListCon.add(MultiList.ImageList(ImageBinding(image)))
                        }
                        "text" -> {
                            val text = listResponse[i]?.text.toString()
                            val textFormatted = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trimStart().trimEnd()
                            iListCon.add(MultiList.TextList(TextBinding(textFormatted)))
                        }
                        "media_collection" -> {
                            for (element in listResponse[i]?.components!!) {
                                val collectionItem = element?.src
                                iListCon.add(MultiList.ImageList(ImageBinding(collectionItem)))
                            }
                        }
                    }
                    i + 1
                }
                myCallBack.invoke(iListCon)
            }
        })
    }

    fun commentsRetrofit(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit) {

        val call = service()?.getComments(projectId.toString())

        call?.enqueue(object : Callback<CommentsMain> {
            override fun onFailure(call: Call<CommentsMain>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<CommentsMain>,
                response: retrofit2.Response<CommentsMain>
            ) {

                val listResponse = response.body()?.comments
                val numberOfComments = response.body()?.comments?.size

                if (numberOfComments == 0) {
                    iListCom.add(MultiList.CountList(CountBinding(numberOfComments.toInt())))
                } else {
                    if (listResponse != null) for (i in 0 until numberOfComments!!) {

                        val commentsAvatarView = listResponse[i]?.user?.images?.jsonMember138
                        val commentatorName = listResponse[i]?.user?.displayName
                        val commentatorUsername = listResponse[i]?.user?.username
                        val comment = listResponse[i]?.comment
                        val date = listResponse[i]?.createdOn.toString()

                        if (i == 0) {
                            iListCom.add(MultiList.CountList(CountBinding(numberOfComments)))
                        }
                        iListCom.add(
                            MultiList.CommentsList(
                                CommentsBinding(
                                    commentsAvatarView,
                                    commentatorName,
                                    commentatorUsername,
                                    comment,
                                    date
                                )
                            )
                        )
                        i + 1
                    }
                }
                myCallBack.invoke(iListCom)
            }

        })

    }

    fun userRetrofit(
        username: String,
        myCallBack: (result: MutableList<ProfileBinding>, links: MutableMap<String, String?>) -> Unit
    ) {

        val call = service()?.getUser(username)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<UserResponse>,
                response: retrofit2.Response<UserResponse>
            ) {
                val listResponse = response.body()?.user

                val id = listResponse?.id
                val avatar = listResponse?.images?.jsonMember276
                val thumbnail = listResponse?.images?.jsonMember100
                val artistName = listResponse?.displayName
                val cityCountry = listResponse?.city + ", " + listResponse?.country
                val views = listResponse?.stats?.views
                val appreciations = listResponse?.stats?.appreciations
                val followers = listResponse?.stats?.followers
                val following = listResponse?.stats?.following
                var aboutArtist = listResponse?.sections?.aboutMe?.replace("\n", "")
                val post = with(StringBuilder()) {
                    if (listResponse != null) if (listResponse.fields?.size!! == 0) {
                        append("Nothing here")
                    } else {
                        (listResponse.fields.indices).forEach { i ->
                            if (i == listResponse.fields.size - 1) {
                                append(listResponse.fields[i])
                            } else {
                                append(listResponse.fields[i] + ", ")
                            }
                        }
                    }
                    toString()
                }
                val url = listResponse?.url


                if (listResponse?.hasSocialLinks == true) {
                    for (i in listResponse.socialLinks!!.indices) {
                        with(listResponse.socialLinks[i]) {
                            when (this?.serviceName) {
                                "Pinterest" -> {
                                    val pinterest = this.url
                                    linksList.put("Pinterest", pinterest)
                                }
                                "Instagram" -> {
                                    val instagram = this.url
                                    linksList.put("Instagram", instagram)
                                }
                                "Facebook" -> {
                                    val facebook = this.url
                                    linksList.put("Facebook", facebook)
                                }
                                "Behance" -> {
                                    val behance = this.url
                                    linksList.put("Behance", behance)
                                }
                                "Dribbble" -> {
                                    val dribbble = this.url
                                    linksList.put("Dribbble", dribbble)
                                }
                                "Twitter" -> {
                                    val twitter = this.url
                                    linksList.put("Twitter", twitter)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }


                if (aboutArtist == null) aboutArtist = "No information"

                userList.add(
                    ProfileBinding(
                        id,
                        avatar,
                        thumbnail,
                        artistName,
                        cityCountry,
                        views.toString(),
                        appreciations.toString(),
                        followers.toString(),
                        following.toString(),
                        aboutArtist,
                        post,
                        url
                    )
                )

                myCallBack.invoke(userList, linksList)
            }
        })

    }

    private fun service(): BehanceApiInterface? {
        return with(NetworkModule()) { providesBehanceApi(providesRetrofit(providesOkHttpClient().build())) }
    }

}