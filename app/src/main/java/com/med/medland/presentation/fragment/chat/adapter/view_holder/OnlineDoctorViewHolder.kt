package com.med.medland.presentation.fragment.chat.adapter.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.med.medland.databinding.ItemOnlineDoctorsBinding
import com.med.medland.domain.model.Doctor

class OnlineDoctorViewHolder(
    private val binding: ItemOnlineDoctorsBinding
): ViewHolder(binding.root) {

    fun onBind(item: Doctor) {

    }

    companion object {
        fun create(viewGroup: ViewGroup): OnlineDoctorViewHolder {
            return OnlineDoctorViewHolder(
                ItemOnlineDoctorsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}