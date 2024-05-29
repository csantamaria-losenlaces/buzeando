package com.carlossantamaria.buzeando.conversations

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.ConversationItem

class ConversationsListViewHolder(
    view: View,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val tvNombreApellidos: TextView = view.findViewById(R.id.tvNombreApellidos)
    private val tvUltimoMensaje: TextView = view.findViewById(R.id.tvUltimoMensaje)
    private val tvHoraUltimoMensaje: TextView = view.findViewById(R.id.tvHoraUltimoMensaje)

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    fun createItems(item: ConversationItem) {
        tvNombreApellidos.text = buildString {
            append(item.nombreReceptor)
            append(" ")
            append(item.apellidosReceptor)
        }
        tvUltimoMensaje.text = item.ultimoMensaje
        tvHoraUltimoMensaje.text = item.horaUltimoMensaje
    }

}