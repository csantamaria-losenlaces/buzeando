package com.carlossantamaria.buzeando.objects

data class ConversationItem (
    var idConversacion: Int,
    var idReceptor: Int,
    var nombreReceptor: String,
    var apellidosReceptor: String,
    var ultimoMensaje: String,
    var horaUltimoMensaje: String
)