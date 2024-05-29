package com.carlossantamaria.buzeando.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.carlossantamaria.buzeando.objects.Offer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LoadOffersFromDb {
    fun cargarOfertas(context: Context, callback: (MutableList<Offer>) -> Unit) {
        val url = "http://77.90.13.129/android/getoffers.php"
        val requestQueue = Volley.newRequestQueue(context)
        val offerList = mutableListOf<Offer>()

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                Log.i("Volley", response.toString())
                (0 until response.length()).forEach {
                    val offer = response.getJSONObject(it)
                    Log.i("Volley", "Título de la oferta: ${offer.get("titulo")}")
                    offerList.add(
                        Offer(
                            offer.get("id_oferta").toString().toInt(),
                            offer.get("id_usr").toString().toInt(),
                            offer.get("tipo").toString(),
                            LocalDateTime.parse(
                                offer.get("fecha").toString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            ),
                            offer.get("titulo").toString(),
                            offer.get("descripcion").toString(),
                            offer.get("coste").toString().toDouble(),
                            offer.get("cod_postal").toString(),
                            offer.get("ruta_img_1").toString(),
                            offer.get("ruta_img_2").toString(),
                            offer.get("ruta_img_3").toString(),
                            offer.get("coords_lat").toString().toDouble(),
                            offer.get("coords_long").toString().toDouble()
                        )
                    )
                }
                callback(offerList)
            },
            { error ->
                Toast.makeText(context, "No se han podido cargar las ofertas", Toast.LENGTH_SHORT)
                    .show()
                Log.i("Volley", error.message.toString())
            }
        )
        requestQueue.add(jsonArrayRequest)
    }
}