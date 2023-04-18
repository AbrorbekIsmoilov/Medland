package com.med.medland.presentation.fragment.chatFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.med.medland.databinding.ItemRecentChatBinding
import com.med.medland.presentation.fragment.otherComponents.SetCustomization.margin

class RecentChatsAdapter : RecyclerView.Adapter<RecentChatsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecentChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemCount : Int) {
            if (itemCount == 7) {
                itemView.margin(20F,5F,20F,10F)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRecentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = 8

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

}