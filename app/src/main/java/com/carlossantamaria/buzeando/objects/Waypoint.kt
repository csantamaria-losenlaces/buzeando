package com.carlossantamaria.buzeando.objects

import com.google.android.gms.maps.model.LatLng

data class Waypoint(
    val titulo: String,
    val descripcion: String,
    val coordenadas: LatLng
)