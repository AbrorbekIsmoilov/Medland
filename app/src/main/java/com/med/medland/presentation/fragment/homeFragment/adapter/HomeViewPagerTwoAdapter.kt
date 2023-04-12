package com.med.medland.presentation.fragment.homeFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.databinding.ItemViewPagerTwoInHomeBinding
import com.med.medland.presentation.fragment.homeFragment.model.ViewPagerTwoModel

class HomeViewPagerTwoAdapter(private val itemList : List<ViewPagerTwoModel>) :
    RecyclerView.Adapter<HomeViewPagerTwoAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ItemViewPagerTwoInHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : ViewPagerTwoModel) {
            //Glide.with(binding.root.context).load(item.image).into(binding.imageTitleInViewpagerItem)
            binding.tvTitleOneInViewpagerItem.text = item.title_one
            binding.tvTitleTwoInViewpagerItem.text = item.title_two
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemViewPagerTwoInHomeBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }
}