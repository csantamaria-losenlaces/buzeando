package com.carlossantamaria.buzeando.chat

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.Message
import org.json.JSONArray

class LoadMessages(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun loadMessages(conversationId: Int, callback: (List<Message>) -> Unit) {
        val url = "https://tu-dominio.com/get_messages.php?conversation_id=$conversationId"
        val getRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonResponse = JSONArray(response)
                val messages = mutableListOf<Message>()
                for (i in 0 until jsonResponse.length()) {
                    val messageObj = jsonResponse.getJSONObject(i)
                    val id = messageObj.getInt("id")
                    val senderId = messageObj.getInt("sender_id")
                    val message = messageObj.getString("message")
                    val sentAt = messageObj.getString("sent_at")
                    messages.add(Message(id, conversationId, senderId, message, sentAt))
                }
                callback(messages)
            },
            { error ->
                error.printStackTrace()
            })
        queue.add(getRequest)
    }
}