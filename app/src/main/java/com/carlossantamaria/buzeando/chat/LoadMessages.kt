package com.carlossantamaria.buzeando.chat

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.ChatModel
import com.carlossantamaria.buzeando.objects.User
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoadMessages(context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun loadMessages(conversationId: Int, callback: (MutableList<ChatModel>) -> Unit) {
        val url = "http://77.90.13.129/android/getmessages.php?conversation_id=$conversationId"
        val getRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonResponse = JSONArray(response)
                val messages = mutableListOf<ChatModel>()
                for (i in 0 until jsonResponse.length()) {
                    val messageObj = jsonResponse.getJSONObject(i)
                    val message = messageObj.getString("texto")
                    val time = messageObj.getString("fecha")

                    val dateTime = LocalDateTime.parse(
                        time,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    )
                    val dateTimeFormateado =
                        dateTime.format(DateTimeFormatter.ofPattern("d/MM/yy HH:mm"))

                    val senderId = messageObj.getString("id_usr")
                    val type = if (User.id_usr == senderId) "SEND" else "RECEIVE"
                    messages.add(
                        ChatModel(
                            type,
                            message,
                            dateTimeFormateado,
                            Integer.parseInt(senderId)
                        )
                    )
                }
                callback(messages)
            },
            { error ->
                error.printStackTrace()
            })
        queue.add(getRequest)
    }
}