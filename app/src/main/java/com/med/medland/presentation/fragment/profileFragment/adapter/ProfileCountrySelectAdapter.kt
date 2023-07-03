package com.med.medland.presentation.fragment.otherComponents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.med.medland.R
import com.med.medland.databinding.ItemCountryFlagAndCodeBinding
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel

class ProfileCountrySelectAdapter(private var countryList : ArrayList<PhoneMaskModel>,val selectListener : ProfileSelectList, val codeShow : Boolean)
    : RecyclerView.Adapter<ProfileCountrySelectAdapter.ViewHolder>() {



     inner class ViewHolder(val binding : ItemCountryFlagAndCodeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCountryFlagAndCodeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countryList[position]
        holder.binding.apply {
            countryName.text = item.name
            Glide.with(root.context).load(item.imageUrl).error(R.drawable.ic_online)
                .into(coountryFlag)
            if (codeShow) countryPhoneCode.text = item.countryCode
            root.setOnClickListener {
                selectListener.selectedCountry(item)

            }
        }

    }
    override fun getItemCount() = countryList.size





fun filteredList(searchedList : ArrayList<PhoneMaskModel>) {
     countryList= searchedList
    notifyDataSetChanged()
}

interface ProfileSelectList {
    fun selectedCountry(selected : PhoneMaskModel)

}

}

