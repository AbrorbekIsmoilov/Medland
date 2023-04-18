package com.med.medland.presentation.fragment.chatFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.med.medland.databinding.ItemOnlineDoctorsBinding
import com.med.medland.presentation.fragment.otherComponents.SetCustomization.margin

class OnlineDoctorsAdapter : RecyclerView.Adapter<OnlineDoctorsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOnlineDoctorsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemCount : Int) {
            if (itemCount == 3) {
                itemView.margin(20F,10F,20F,10F)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOnlineDoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = 4

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }

}