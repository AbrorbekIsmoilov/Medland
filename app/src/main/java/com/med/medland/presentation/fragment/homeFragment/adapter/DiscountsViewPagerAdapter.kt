package com.med.medland.presentation.fragment.homeFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.databinding.ItemDiscountInHomeBinding
import com.med.medland.databinding.ItemInterviewBinding
import com.med.medland.presentation.fragment.homeFragment.model.DiscountModel

class DiscountsViewPagerAdapter(private val discountList : List<DiscountModel>) :
 RecyclerView.Adapter<DiscountsViewPagerAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ItemDiscountInHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : DiscountModel) {
            Glide.with(binding.root.context).load(item.image).into(binding.discountImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDiscountInHomeBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount() = discountList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(discountList[position])
    }
}