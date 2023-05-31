package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.chat.adapters.listeners.OnMessageClickListener
import com.example.chat.databinding.ItemSentMessageBinding
import uz.crud.data.model.ChatData

class SentViewHolder(private val binding: ItemSentMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData, onMessageClickListener: OnMessageClickListener) {
        binding.apply {
            val date = item.date
            dateTv.text = date
            sentMessage.text = item.name

            root.setOnClickListener {
                if (!item.name.isNullOrEmpty()) {
                    onMessageClickListener.onClick(adapterPosition, item.name!!, item.id)
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SentViewHolder {
            val binding = ItemSentMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return SentViewHolder(binding)
        }
    }
}