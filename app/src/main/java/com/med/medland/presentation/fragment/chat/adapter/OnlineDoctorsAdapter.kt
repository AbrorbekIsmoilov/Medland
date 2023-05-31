package com.med.medland.presentation.fragment.chat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.med.medland.domain.model.Doctor
import com.med.medland.presentation.fragment.chat.adapter.view_holder.OnlineDoctorViewHolder

class OnlineDoctorsAdapter : ListAdapter<Doctor, OnlineDoctorViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineDoctorViewHolder {
        return OnlineDoctorViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OnlineDoctorViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object DiffCallback : ItemCallback<Doctor>() {
        override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem == newItem
        }
    }
}