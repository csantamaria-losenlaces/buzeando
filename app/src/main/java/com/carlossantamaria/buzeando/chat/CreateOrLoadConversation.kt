package com.carlossantamaria.buzeando.chat

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CreateOrLoadConversation(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun createOrLoadConversation(user1Id: Int, user2Id: Int, callback: (Int) -> Unit) {
        val url = "https://tu-dominio.com/create_conversation.php"
        val postRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val conversationId = jsonResponse.getInt("conversation_id")
                callback(conversationId)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("user1_id" to user1Id.toString(), "user2_id" to user2Id.toString())
            }
        }
        queue.add(postRequest)
    }
}