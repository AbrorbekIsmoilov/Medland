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

class SelectCountryCodeAdapter(private var countryList : ArrayList<PhoneMaskModel>,
                               val selectListener : SelectedCountryListener, val codeShow : Boolean)
    : RecyclerView.Adapter<SelectCountryCodeAdapter.ViewHolder>() {

    interface SelectedCountryListener {
        fun selectedCountry(selected : PhoneMaskModel)
    }

    class ViewHolder(val binding : ItemCountryFlagAndCodeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCountryFlagAndCodeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount() = countryList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countryList[position]
        holder.binding.apply {
            countryName.text = item.name
            Glide.with(root.context).load(item.imageUrl).error(R.drawable.ic_online).into(coountryFlag)
            if (codeShow) countryPhoneCode.text = item.countryCode
            root.setOnClickListener {
                selectListener.selectedCountry(item)
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun filteredList(searchedList : ArrayList<PhoneMaskModel>) {
        countryList = searchedList
        notifyDataSetChanged()
    }

}