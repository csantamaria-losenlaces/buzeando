package com.carlossantamaria.buzeando.objects

import java.time.LocalDateTime

data class Offer(
    val idOferta: Int,
    val idUsr: Int,
    val tipo: String,
    val fecha: LocalDateTime,
    val titulo: String,
    val descripcion: String,
    val coste: Double,
    val codPostal: String,
    val rutaImg1: String,
    val rutaImg2: String,
    val rutaImg3: String,
    val coordsLat: Double,
    val coordsLong: Double
)