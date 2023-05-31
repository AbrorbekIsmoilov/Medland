package com.crudgroup.chat.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.crudgroup.chat.adapters.holders.*
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.crudgroup.chat.adapters.listeners.OnMessageClickListener
import uz.crud.data.model.ChatData

class ChatRecyclerAdapter(
    private val onClickListener: ChatOnClickListener,
    private val onMessageClickListener: OnMessageClickListener
) : ListAdapter<ChatData, ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> SentViewHolder.create(parent)
            2 -> SentImageviewHolder.create(parent)
            3 -> SentPdfViewHolder.create(parent)
            4 -> SentAudioViewHolder.create(parent)
            6 -> ReceivedViewHolder.create(parent)
            7 -> ReceivedImageViewHolder.create(parent)
            8 -> ReceivedPdfViewHolder.create(parent)
            9 -> ReceivedAudioViewHolder.create(parent)
            else -> throw IllegalArgumentException("error")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ChatData = getItem(position)
        when (holder) {
            is SentViewHolder -> holder.bind(item, onMessageClickListener)
            is SentImageviewHolder -> holder.bind(item, onClickListener)
            is SentPdfViewHolder -> holder.bind(item, onClickListener)
            is SentAudioViewHolder -> holder.bind(item, onClickListener)
            is ReceivedViewHolder -> holder.bind(item)
            is ReceivedImageViewHolder -> holder.bind(item, onClickListener)
            is ReceivedPdfViewHolder -> holder.bind(item, onClickListener)
            is ReceivedAudioViewHolder -> holder.bind(item, onClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item: ChatData = getItem(position)
        return if (item.userDoctor == 1) {
            item.type
        } else {
            (item.type + 5)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatData>() {
        override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean =
            ((oldItem.name == newItem.name) && (oldItem.downloadedUri == newItem.downloadedUri)
                    && (oldItem.url == newItem.url))
    }
}