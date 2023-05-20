package com.med.medland.presentation.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.databinding.ItemDoctorsInCategoriesBinding
import com.med.medland.presentation.fragment.home.model.DoctorsModel

class DoctorsAdapter(private val itemList : List<DoctorsModel>) :
    RecyclerView.Adapter<DoctorsAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ItemDoctorsInCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : DoctorsModel) {
            Glide.with(binding.root.context).load(item.image).into(binding.imageItemDoctor)
            binding.tvNameItemDoctor.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDoctorsInCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }
}