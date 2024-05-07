package com.carlossantamaria.buzeando.objects

import com.google.android.gms.maps.model.LatLng

data class Waypoint(
    val oferta: Offer,
    val titulo: String,
    val descripcion: String,
    val coordenadas: LatLng
)