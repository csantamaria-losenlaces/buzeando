package com.carlossantamaria.buzeando

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OfferListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTipoOferta: TextView = view.findViewById(R.id.tvTipoOferta)
    private val ivImagenPrincipal: ImageView = view.findViewById(R.id.ivImagenPrincipal)
    private val tvTituloOferta: TextView = view.findViewById(R.id.tvTituloOferta)
    private val tvDescOferta: TextView = view.findViewById(R.id.tvDescOferta)
    private val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)

    fun createItems(item: Offer) {
        tvTipoOferta.text = item.tipo
        tvTituloOferta.text = item.titulo
        tvDescOferta.text = item.descripcion
        tvPrecio.text = item.precio.toString()
        Picasso.get()
            .load("https://s3.amazonaws.com/www-inside-design/uploads/2018/12/The-product-of-you-810x810.png")
            .resize(88, 88)
            .centerCrop()
            .into(ivImagenPrincipal)
    }

}