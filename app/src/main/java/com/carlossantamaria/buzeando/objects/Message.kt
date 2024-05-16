package com.carlossantamaria.buzeando.objects

data class Message(
    val id: Int,
    val conversationId: Int,
    val senderId: Int,
    val message: String,
    val sentAt: String
)