package com.example.bestofbehance.viewModels

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bestofbehance.gson.*
import com.google.gson.Gson
import org.json.JSONObject

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val comList: MutableList<CommentsBinding> = mutableListOf()
    private var description: String? = ""
    private val apiKey = "0QmPh684DRz1SpWHDikkyFCzLShGiHPi"

    fun parseGeneral(myCallBack: (result: MutableList<CardBinding>) -> Unit){
        val url = "https://api.behance.net/v2/projects?sort=appreciations&api_key=$apiKey"
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
                    recList.add(CardBinding(Arts, Avatars, Names, Posts, Views.toString(), Appreciations.toString(), Comments.toString(), Id.toString()))
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

    fun parseDescription(projectId: Int, myCallBack: (result: String?) -> Unit){
        val url = "https://www.behance.net/v2/projects/$projectId?api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), Description::class.java)


                description = responce_gson.project?.description

                myCallBack.invoke(description)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()
    }

    fun parseComments(projectId: Int, myCallBack: (result: MutableList<CommentsBinding>) -> Unit){
        val url = "https://www.behance.net/v2/projects/$projectId/comments?api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), CommentsMain::class.java)
                val jsonArray = responce.getJSONArray("comments")
                for (i in 0 until jsonArray.length()) {

                    val commentsAvatarView = responce_gson.comments?.get(i)?.user?.images?.jsonMember138
                    val commentsName =  responce_gson.comments?.get(i)?.user?.displayName
                    val comment = responce_gson.comments?.get(i)?.comment
                    val date = responce_gson.comments?.get(i)?.createdOn.toString()
                    val numberOfComments = jsonArray.length().toString()

                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, id))
                    comList.add(CommentsBinding(commentsAvatarView, commentsName, comment, date, numberOfComments))
                    i + 1
                }
                myCallBack.invoke(comList)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()
    }
}