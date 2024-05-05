package com.carlossantamaria.buzeando.offerlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Offer

class OfferListAdapter (private var listaOfertas: List<Offer>) : RecyclerView.Adapter<OfferListViewHolder> () {

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

    @SuppressLint("NotifyDataSetChanged")
    fun update(listaOfertasNueva:MutableList<Offer>) {
        listaOfertas = listaOfertasNueva
        this.notifyDataSetChanged()
    }

}