package com.carlossantamaria.buzeando.objects

data class ChatModel(
    var type: String,
    var message: String,
    var time: String,
    val senderId: Int
)