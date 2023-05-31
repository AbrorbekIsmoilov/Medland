package com.med.medland.presentation.fragment.chat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.med.medland.domain.model.Doctor
import com.med.medland.presentation.fragment.chat.adapter.view_holder.RecentMessagedDoctorViewHolder

class RecentChatsAdapter :
    ListAdapter<Doctor, RecentMessagedDoctorViewHolder>(OnlineDoctorsAdapter.DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentMessagedDoctorViewHolder {
        return RecentMessagedDoctorViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecentMessagedDoctorViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}