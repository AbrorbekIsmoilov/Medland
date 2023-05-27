package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.example.chat.R
import com.example.chat.databinding.ItemSentImageBinding
import uz.crud.data.model.ChatData

class SentImageviewHolder(
    private val binding: ItemSentImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData, listener: ChatOnClickListener) {
        binding.apply {
            val date = item.date?.replace('T', ' ')
            dateTv.text = date
            sentImage.load(item.url) {
                crossfade(true)
                placeholder(R.drawable.ic_account)
            }
            sentImage.setOnClickListener { listener.onClick(item) }
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): SentImageviewHolder =
            SentImageviewHolder(
                ItemSentImageBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }
}