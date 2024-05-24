package com.carlossantamaria.buzeando.offerlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.Offer

class OfferListAdapter(
    private var listaOfertas: List<Offer>, private val onItemClicked: (Offer) -> Unit
) : RecyclerView.Adapter<OfferListViewHolder>(), Filterable {

    private var listaOfertasOriginal = listOf<Offer>()

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(input: CharSequence): FilterResults {
            val filteredList = if (input.isEmpty()) {
                listaOfertasOriginal
            } else {
                listaOfertasOriginal.filter { it.titulo.lowercase().contains(input) }
            }
            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(input: CharSequence, results: FilterResults) {
            updateData(results.values as MutableList<Offer>)
        }
    }

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

    fun setData(list: MutableList<Offer>) {
        this.listaOfertasOriginal = list
        updateData(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listaOfertasNueva: MutableList<Offer>) {
        listaOfertas = listaOfertasNueva
        this.notifyDataSetChanged()
    }

    override fun getFilter(): Filter = searchFilter

}