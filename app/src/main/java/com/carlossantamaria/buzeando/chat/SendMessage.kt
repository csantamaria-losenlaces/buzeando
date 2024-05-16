package com.carlossantamaria.buzeando.chat

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SendMessage(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun sendMessage(conversationId: Int, senderId: Int, message: String, callback: (Boolean) -> Unit) {
        val url = "https://tu-dominio.com/send_message.php"
        val postRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                callback(success)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "conversation_id" to conversationId.toString(),
                    "sender_id" to senderId.toString(),
                    "message" to message
                )
            }
        }
        queue.add(postRequest)
    }
}