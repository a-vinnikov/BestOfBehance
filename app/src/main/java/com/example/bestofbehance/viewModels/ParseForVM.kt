package com.example.bestofbehance.viewModels

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.binding.CommentsBinding
import com.example.bestofbehance.binding.ImageBinding
import com.example.bestofbehance.gson.*
import com.google.gson.Gson
import org.json.JSONObject

class ParseForVM {

    private val recList: MutableList<CardBinding> = mutableListOf()
    private val imageList: MutableList<ImageBinding> = mutableListOf()
    private val comList: MutableList<CommentsBinding> = mutableListOf()
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

    fun parseProject(projectId: Int, myCallBack: (result: MutableList<ImageBinding>) -> Unit){
        val url = "https://www.behance.net/v2/projects/$projectId?api_key=$apiKey"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { responce ->
                val responce_gson = Gson().fromJson(responce.toString(), ImageResponce::class.java)
                //val jsonArray = responce.getJSONArray("project")
                for (i in 0 until responce_gson.project?.modules!!.size) {

                    val image = responce_gson.project.modules[i]?.sizes?.disp
                    val description = responce_gson.project.description

                    if (i < responce_gson.project.modules.size -1 ){
                        imageList.add(ImageBinding(image, null))
                    }else{
                        imageList.add(ImageBinding(image, description))
                    }
                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, id))
                    /*if (i < jsonArray.length()-1){
                        imageList.add(ImageBinding(image, null))
                    } else if (i == jsonArray.length()-1) {
                        imageList.add(ImageBinding(image, description))
                    }*/
                    i + 1
                }
                myCallBack.invoke(imageList)

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
                    if (i == 0){
                        val temp = "Comments($numberOfComments)"
                        comList.add(CommentsBinding(null, null, temp, null))
                    }
                    comList.add(
                        CommentsBinding(
                            commentsAvatarView,
                            commentsName,
                            comment,
                            date
                        )
                    )
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