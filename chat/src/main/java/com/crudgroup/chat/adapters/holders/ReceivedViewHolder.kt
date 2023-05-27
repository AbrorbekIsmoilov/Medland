package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ItemReceivedMessageBinding
import uz.crud.data.model.ChatData

class ReceivedViewHolder(private val binding: ItemReceivedMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData) {
        binding.apply {
            val date = item.date?.replace('T', ' ')
            dateTv.text = date
            receivedMessage.text = item.name
        }
    }

    companion object {
        fun create(parent: ViewGroup): ReceivedViewHolder {
            val binding = ItemReceivedMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ReceivedViewHolder(binding)
        }
    }
}