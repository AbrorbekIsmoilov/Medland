package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.chat.adapters.listeners.ChatOnClickListener
import com.example.chat.R
import com.example.chat.databinding.ItemSentPdfBinding
import uz.crud.data.model.ChatData

class SentPdfViewHolder(
    private val binding: ItemSentPdfBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatData, onClickListener: ChatOnClickListener) {
        if (item.downloadedUri.isNullOrEmpty()) {
            binding.downloadIv.setImageResource(R.drawable.ic_download)
        } else {
            binding.downloadIv.setImageResource(R.drawable.ic_file_download_done)
        }
        val date = item.date?.replace('T', ' ')
        binding.dateTv.text = date
        binding.fileTitle.text = item.name
        binding.downloadIv.setOnClickListener {
            onClickListener.onClick(item)
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): SentPdfViewHolder {
            return SentPdfViewHolder(
                ItemSentPdfBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}