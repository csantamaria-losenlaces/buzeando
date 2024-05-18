package com.carlossantamaria.buzeando.chat

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ChatWebSocket(private val listener: ChatWebSocketListener) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect() {
        val request = Request.Builder().url("ws://77.90.13.129:8080/android").build()
        webSocket = client.newWebSocket(request, SocketListener())
        client.dispatcher.executorService.shutdown()
    }

    fun send(message: String) {
        webSocket.send(message)
    }

    fun close() {
        webSocket.close(1000, "Goodbye")
    }

    private inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            listener.onOpen()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listener.onMessage(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            listener.onMessage(bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            listener.onClosing(code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            listener.onFailure(t)
        }
    }

    interface ChatWebSocketListener {
        fun onOpen()
        fun onMessage(text: String)
        fun onClosing(code: Int, reason: String)
        fun onFailure(t: Throwable)
    }
}