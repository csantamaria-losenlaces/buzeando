package com.carlossantamaria.buzeando.objects

class User {

    companion object {
        var id_usr: String = ""
        var nombre: String = ""
        var apellidos: String = ""
        var dir: String = ""
        var cod_postal: String = ""
        var movil: String = ""
        var mail: String = ""
        var hash_pwd: String = ""

        fun cerrarSesion() {
            id_usr = ""
            nombre = ""
            apellidos = ""
            dir = ""
            cod_postal = ""
            movil = ""
            mail = ""
            hash_pwd = ""
        }
    }

}