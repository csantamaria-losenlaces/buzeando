package com.carlossantamaria.buzeando.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carlossantamaria.buzeando.databinding.MessageInItemBinding
import com.carlossantamaria.buzeando.databinding.MessageOutItemBinding
import com.carlossantamaria.buzeando.objects.ChatModel

class ChatAdapterViewHolder : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listOfChat = mutableListOf<ChatModel>()

    private val leftView = 0
    private val rightView = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            leftView -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MessageInItemBinding.inflate(inflater, parent, false)
                LeftViewHolder(binding)
            }

            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MessageOutItemBinding.inflate(inflater, parent, false)
                RightViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = listOfChat.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listOfChat[position].type == "SEND")
            (holder as RightViewHolder).bind(listOfChat[position])
        else
            (holder as LeftViewHolder).bind(listOfChat[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfChat[position].type == "SEND") rightView else leftView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<ChatModel>) {
        listOfChat.clear()
        listOfChat.addAll(list)
        notifyDataSetChanged()
    }

    fun addMessage(chatModel: ChatModel) {
        listOfChat.add(chatModel)
        notifyItemInserted(listOfChat.size - 1)
    }

    inner class LeftViewHolder(private val binding: MessageInItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatModel) {
            binding.apply {
                chat.also {
                    tvMensaje.text = chat.message
                    tvHora.text = chat.time
                }
            }
        }
    }

    inner class RightViewHolder(private val binding: MessageOutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatModel) {
            binding.apply {
                chat.also {
                    tvMensaje.text = chat.message
                    tvHora.text = chat.time
                }
            }
        }
    }

}