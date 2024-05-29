package com.carlossantamaria.buzeando.conversations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.R
import com.carlossantamaria.buzeando.objects.ConversationItem

class ConversationsListAdapter(
    private var listaConversaciones: List<ConversationItem>,
    private val onItemClicked: (ConversationItem) -> Unit
) : RecyclerView.Adapter<ConversationsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return ConversationsListViewHolder(view) { clickedItemPosition ->
            onItemClicked(listaConversaciones[clickedItemPosition])
        }
    }

    override fun getItemCount(): Int = listaConversaciones.size

    override fun onBindViewHolder(holder: ConversationsListViewHolder, position: Int) {
        holder.createItems(listaConversaciones[position])
    }

    fun update(listaConversacionesNueva: MutableList<ConversationItem>) {
        listaConversaciones = listaConversacionesNueva
        notifyItemRangeInserted(listaConversaciones.size - 1, listaConversacionesNueva.size)
    }

}