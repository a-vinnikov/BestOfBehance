package com.example.bestofbehance.viewModel

import android.content.Context
import androidx.core.text.HtmlCompat
import com.example.bestofbehance.R
import com.example.bestofbehance.retrofit.BehanceApiInterface
import com.example.bestofbehance.binding.*
import com.example.bestofbehance.binding.detailsBinding.CommentsBinding
import com.example.bestofbehance.binding.detailsBinding.CountBinding
import com.example.bestofbehance.binding.detailsBinding.ImageBinding
import com.example.bestofbehance.binding.detailsBinding.TextBinding
import com.example.bestofbehance.dagger.module.NetworkModule
import com.example.bestofbehance.classesToSupport.MultiList
import java.lang.StringBuilder

class ParseForViewModel(val context: Context) {

    private val iListCon: MutableList<MultiList> = mutableListOf()
    private val iListCom: MutableList<MultiList> = mutableListOf()
    private val userList: MutableList<ProfileBinding> = mutableListOf()
    private val linksList: MutableMap<String, String?> = mutableMapOf()
    private val singleList: MutableList<CardBinding> = mutableListOf()


    fun fetchProject(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit) {

        service(context)?.getProject(projectId)?.process { imageResponse, _ ->
            val listResponse = imageResponse?.project?.modules

            if (listResponse != null) for (i in listResponse.indices) {

                when (listResponse[i]!!.type) {
                    context.resources.getString(R.string.image) -> {
                        val image = listResponse[i]?.src
                        iListCon.add(MultiList.ImageList(
                            ImageBinding(
                                image
                            )
                        ))
                    }
                    context.resources.getString(R.string.text) -> {
                        val text = listResponse[i]?.text.toString()
                        val textFormatted =
                            HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                .toString().trim()
                        iListCon.add(MultiList.TextList(
                            TextBinding(
                                textFormatted
                            )
                        ))
                    }
                    context.resources.getString(R.string.media_collection) -> {
                        for (element in listResponse[i]?.components!!) {
                            val collectionItem = element?.src
                            iListCon.add(MultiList.ImageList(
                                ImageBinding(
                                    collectionItem
                                )
                            ))
                        }
                    }
                }
                i + 1
            }
            myCallBack.invoke(iListCon)
        }
    }

    fun fetchComments(projectId: Int, myCallBack: (result: MutableList<MultiList>) -> Unit) {

        service(context)?.getComments(projectId.toString())?.process { commentsMain, _ ->
            val listResponse = commentsMain?.comments
            val numberOfComments = commentsMain?.comments?.size

            if (numberOfComments == 0) {
                iListCom.add(MultiList.CountList(
                    CountBinding(
                        numberOfComments.toInt()
                    )
                ))
            } else {
                listResponse.let {
                    for (i in 0 until numberOfComments!!) {

                        val commentsAvatarView = it?.get(i)?.user?.images?.jsonMember138
                        val commentatorName = it?.get(i)?.user?.displayName
                        val commentatorUsername = it?.get(i)?.user?.username
                        val comment = it?.get(i)?.comment
                        val date = it?.get(i)?.createdOn.toString()

                        if (i == 0) {
                            iListCom.add(MultiList.CountList(
                                CountBinding(
                                    numberOfComments
                                )
                            ))
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
            }
            myCallBack.invoke(iListCom)
        }

    }

    fun fetchUser(
        username: String,
        myCallBack: (result: MutableList<ProfileBinding>, links: MutableMap<String, String?>) -> Unit
    ) {

        service(context)?.getUser(username)?.process { userResponse, _ ->

            val listResponse = userResponse?.user

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
                    append(context.resources.getString(R.string.no_information))
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
                            context.resources.getString(R.string.pinterest) -> {
                                val pinterest = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.pinterest),
                                    pinterest
                                )
                            }
                            context.resources.getString(R.string.instagram) -> {
                                val instagram = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.instagram),
                                    instagram
                                )
                            }
                            context.resources.getString(R.string.facebook) -> {
                                val facebook = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.facebook),
                                    facebook
                                )
                            }
                            context.resources.getString(R.string.behance) -> {
                                val behance = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.behance),
                                    behance
                                )
                            }
                            context.resources.getString(R.string.dribbble) -> {
                                val dribbble = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.dribbble),
                                    dribbble
                                )
                            }
                            context.resources.getString(R.string.twitter) -> {
                                val twitter = this?.url
                                linksList.put(
                                    context.resources.getString(R.string.twitter),
                                    twitter
                                )
                            }
                            else -> {
                            }
                        }
                    }
                }
            }


            if (aboutArtist == null) aboutArtist =
                context.resources.getString(R.string.no_information)

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

    }

    fun fetchSingleProject(projectId: Int, myCallBack: (result: MutableList<CardBinding>) -> Unit){

        service(context)?.getProject(projectId)?.process { imageResponse, _ ->

            val listResponse = imageResponse?.project


            val art = listResponse?.covers?.original
            val thumbnail = listResponse?.covers?.jsonMember115
            val artistName = listResponse?.owners?.get(0)?.displayName
            val avatar = listResponse?.owners?.get(0)?.images?.jsonMember138
            val appreciations = listResponse?.stats?.appreciations
            val views = listResponse?.stats?.views
            val comments = listResponse?.stats?.comments
            val artName = listResponse?.name
            val username = listResponse?.owners?.get(0)?.username
            val published = listResponse?.publishedOn
            val url = listResponse?.url

            singleList.add(
                CardBinding(
                    projectId,
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

            myCallBack.invoke(singleList)
        }

    }

    private fun service(context: Context): BehanceApiInterface? {
        return with(NetworkModule(context)) {
            providesBehanceApi(
                providesRetrofit(
                    providesOkHttpClient().build()
                )
            )
        }
    }

}