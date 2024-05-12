package com.carlossantamaria.buzeando.offerlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Offer
import com.squareup.picasso.Picasso

class OfferListViewHolder(
    view: View,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val tvTipoOferta: TextView = view.findViewById(R.id.tvTipoOferta)
    private val ivImagenPrincipal: ImageView = view.findViewById(R.id.ivImagenPrincipal)
    private val tvTituloOferta: TextView = view.findViewById(R.id.tvTituloOferta)
    private val tvDescOferta: TextView = view.findViewById(R.id.tvDescOferta)
    private val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    fun createItems(item: Offer) {
        tvTipoOferta.text = item.tipo
        tvTituloOferta.text = item.titulo
        tvDescOferta.text = item.descripcion
        tvPrecio.text = item.coste.toString()
        Picasso.get()
            .load("http://77.90.13.129/android/${item.rutaImg1}")
            .resize(88, 88)
            .centerCrop()
            .into(ivImagenPrincipal)
    }
}