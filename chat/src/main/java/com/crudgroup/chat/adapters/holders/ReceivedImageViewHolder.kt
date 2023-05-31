package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.example.chat.R
import com.example.chat.databinding.ItemReceivedImageBinding
import uz.crud.data.model.ChatData

class ReceivedImageViewHolder(
    private val binding: ItemReceivedImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData, listener: ChatOnClickListener) {
        binding.apply {
            val date = item.date?.replace('T', ' ')
            dateTv.text = date

            receivedImage.load(item.url) {
                crossfade(true)
                placeholder(R.drawable.ic_account)
            }
            receivedImage.setOnClickListener { listener.onClick(item) }
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): ReceivedImageViewHolder =
            ReceivedImageViewHolder(
                ItemReceivedImageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }
}