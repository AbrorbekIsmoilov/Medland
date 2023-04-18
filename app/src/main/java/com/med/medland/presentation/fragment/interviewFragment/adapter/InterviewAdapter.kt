package com.med.medland.presentation.fragment.interviewFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.databinding.ItemInterviewBinding
import com.med.medland.presentation.fragment.interviewFragment.model.InterviewItemModel

class InterviewAdapter(private val interviewList : List<InterviewItemModel>) :
 RecyclerView.Adapter<InterviewAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ItemInterviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : InterviewItemModel) {
            //Glide.with(binding.root.context).load(item.image).into(binding.interviewImage)

            binding.interviewImage.setImageResource(item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemInterviewBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount() = interviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(interviewList[position])
    }
}