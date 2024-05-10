package com.carlossantamaria.buzeando.offerlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Offer

class OfferListAdapter(
    private var listaOfertas: List<Offer>,
    private val onItemClicked: (Offer) -> Unit
) : RecyclerView.Adapter<OfferListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_item, parent, false)
        return OfferListViewHolder(view) { clickedItemPosition ->
            onItemClicked(listaOfertas[clickedItemPosition])
        }
    }

    override fun onBindViewHolder(holder: OfferListViewHolder, position: Int) {
        holder.createItems(listaOfertas[position])
    }

    override fun getItemCount(): Int = listaOfertas.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(listaOfertasNueva: MutableList<Offer>) {
        listaOfertas = listaOfertasNueva
        this.notifyDataSetChanged()
    }
}