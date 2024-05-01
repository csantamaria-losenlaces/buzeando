package com.carlossantamaria.buzeando

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OfferListAdapter (private val listaOfertas: List<Offer>) : RecyclerView.Adapter<OfferListViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oferta, parent, false)
        return OfferListViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferListViewHolder, position: Int) {
        holder.createItems(listaOfertas[position])
    }

    override fun getItemCount(): Int {
        return listaOfertas.size
    }

}