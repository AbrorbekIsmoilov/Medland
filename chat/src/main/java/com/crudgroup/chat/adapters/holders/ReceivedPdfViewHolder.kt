package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.example.chat.R
import com.example.chat.databinding.ItemReceivedPdfBinding
import uz.crud.data.model.ChatData

class ReceivedPdfViewHolder(
    private val binding: ItemReceivedPdfBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData, onClickListener: ChatOnClickListener) {
        if (item.downloadedUri.isNullOrEmpty()) {
            binding.receivedPdfIv.setImageResource(R.drawable.ic_download)
        } else {
            binding.receivedPdfIv.setImageResource(R.drawable.ic_file_download_done)
        }
        val date = item.date?.replace('T', ' ')
        binding.dateTv.text = date
        binding.receivedPdfTv.text = item.name
        binding.receivedPdfIv.setOnClickListener {
            onClickListener.onClick(item)
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): ReceivedPdfViewHolder {
            return ReceivedPdfViewHolder(
                ItemReceivedPdfBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}