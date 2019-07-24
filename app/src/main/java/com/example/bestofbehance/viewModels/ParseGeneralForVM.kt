package com.example.bestofbehance.viewModels

import android.arch.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bestofbehance.gson.CardBinding
import com.example.bestofbehance.gson.Responce
import com.google.gson.Gson
import org.json.JSONObject

class ParseGeneralForVM {

    private val _recList: MutableList<CardBinding> = mutableListOf()
    private val getMostAppreciations = "projects?sort=appreciations&"
    private val apiKey = "0QmPh684DRz1SpWHDikkyFCzLShGiHPi"

    fun parseGeneral(recList: MutableLiveData<MutableList<CardBinding>>): MutableLiveData<MutableList<CardBinding>> {
        val url = "https://api.behance.net/v2/${getMostAppreciations}api_key=$apiKey"
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

                    //mAPIList.add(APIList(Arts, Avatars, Names, Posts, Views, Appreciations, Comments, Id))
                    _recList.add(CardBinding(Arts, Avatars, Names, Posts, Views.toString(), Appreciations.toString(), Comments.toString(), Id.toString()))
                    i + 1
                }
                recList.postValue(_recList)

            }, Response.ErrorListener {
                VolleyLog.e(VolleyLog.TAG, "ERROR")
            }
        )
        VolleySingleton.requestQueue.add(request)
        VolleySingleton.requestQueue.start()

        return recList
    }
}