package com.crudgroup.chat.adapters.holders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crudgroup.chat.adapters.listeners.InvitationOnClickListener
import com.example.chat.databinding.ItemInvitationBinding

class InvitationViewHolder(
    private val binding: ItemInvitationBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(roomId: String?, onClickListener: InvitationOnClickListener) {
        binding.root.setOnClickListener {
            if (!roomId.isNullOrEmpty()) {
                Log.e("TAG", "bind: roomId = ${roomId.substring(4)}")
                onClickListener.onClick(roomId.substring(4))
            }
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): InvitationViewHolder {
            return InvitationViewHolder(
                ItemInvitationBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}