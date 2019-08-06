package com.example.bestofbehance.viewModels

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.CountBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.binding.TextBinding
import com.example.bestofbehance.gson.*
import com.example.bestofbehance.layout.Ilist
import com.google.gson.Gson
import org.json.JSONObject
import org.jsoup.Jsoup

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val iListCon: MutableList<Ilist> = mutableListOf()
    private val iListCom: MutableList<Ilist> = mutableListOf()
    private val apiKey = "0QmPh684DRz1SpWHDikkyFCzLShGiHPi"

    fun parseGeneral(page: Int, myCallBack: (result: MutableList<CardBinding>) -> Unit) {
        val url = "https://api.behance.net/v2/projects?sort=appreciations&page=$page&api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), Responce::class.java)
                val jsonArray = responce.getJSONArray("projects")
                for (i in 0 until jsonArray.length()) {

                    val Arts = responce_gson.projects?.get(i)?.covers?.original
                    val Names = responce_gson.projects?.get(i)?.owners?.get(0)?.displayName
                    val Avatars = responce_gson.projects?.get(i)?.owners?.get(0)?.images?.jsonMember138
                    val Appreciations = responce_gson.projects?.get(i)?.stats?.appreciations
                    val Views = responce_gson.projects?.get(i)?.stats?.views
                    val Comments = responce_gson.projects?.get(i)?.stats?.comments
                    val Posts = responce_gson.projects?.get(i)?.fields?.get(0)
                    val Id = responce_gson.projects?.get(i)?.id

                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, id))
                    recList.add(
                        CardBinding(
                            Id!!,
                            Arts,
                            Avatars,
                            Names,
                            Posts,
                            Views.toString(),
                            Appreciations.toString(),
                            Comments.toString()

                        )
                    )
                    i + 1
                }
                //callback recList
                myCallBack.invoke(recList)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()
    }

    fun parseProject(projectId: Int, myCallBack: (result: MutableList<Ilist>) -> Unit) {
        val url = "https://www.behance.net/v2/projects/$projectId?api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), ImageResponce::class.java)
                for (i in 0 until responce_gson.project?.modules!!.size) {

                    when (responce_gson.project.modules[i]!!.type) {
                        "image" -> {val image = responce_gson.project.modules[i]?.src
                            iListCon.add(Ilist.ImageList(ImageBinding(image)))}
                        "text" -> {val text = responce_gson.project.modules[i]?.text.toString()
                            val text1 = Jsoup.parse(text).text()
                            iListCon.add(Ilist.TextList(TextBinding(text1)))}
                        "media_collection" -> {
                            for (j in 0 until responce_gson.project.modules[i]?.components!!.size){
                                val collectionItem = responce_gson.project.modules[i]?.components!![j]?.src
                                iListCon.add(Ilist.ImageList(ImageBinding(collectionItem)))} } }
                    i + 1
                }
                myCallBack.invoke(iListCon)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()
    }

    fun parseComments(projectId: Int, myCallBack: (result: MutableList<Ilist>) -> Unit) {
        val url = "https://www.behance.net/v2/projects/$projectId/comments?api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), CommentsMain::class.java)
                val jsonArray = responce.getJSONArray("comments")
                if(jsonArray.length() == 0){
                    val temp = "Comments(${jsonArray.length()})"
                    iListCom.add(Ilist.CountList(CountBinding(temp)))
                }
                parse@ for (i in 0 until jsonArray.length()) {

                    val commentsAvatarView = responce_gson.comments?.get(i)?.user?.images?.jsonMember138
                    val commentsName = responce_gson.comments?.get(i)?.user?.displayName
                    val comment = responce_gson.comments?.get(i)?.comment
                    val date = responce_gson.comments?.get(i)?.createdOn.toString()
                    val numberOfComments = jsonArray.length().toString()

                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, id))
                    if (i == 0) {
                        val temp = "Comments($numberOfComments)"
                        iListCom.add(Ilist.CountList(CountBinding(temp)))
                    }
                    iListCom.add(Ilist.CommentsList(CommentsBinding(commentsAvatarView, commentsName, comment, date)))
                    i + 1
                }
                myCallBack.invoke(iListCom)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()
    }
}