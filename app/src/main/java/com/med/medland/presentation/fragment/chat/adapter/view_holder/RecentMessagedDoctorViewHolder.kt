package com.med.medland.presentation.fragment.chat.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.ItemRecentChatBinding
import com.med.medland.domain.model.Doctor

class RecentMessagedDoctorViewHolder(
    private val binding: ItemRecentChatBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: Doctor) {
        with(binding) {
            Glide.with(binding.root)
                .load(Constants.BASE_URL_IMAGES + item.rasm)
                .centerCrop()
                .into(selectContactImage)

            recentDoctorName.text = "${item.familiya} ${item.ism}"
            tvUnreadMsgCount.text = item.doctor_count.toString()
            tvLastMsgTime.text = item.online
        }
    }

    companion object {
        fun create(parent: ViewGroup): RecentMessagedDoctorViewHolder {
            return RecentMessagedDoctorViewHolder(
                ItemRecentChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}