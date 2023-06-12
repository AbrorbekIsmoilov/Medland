package com.med.medland.presentation.fragment.otherComponents.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.med.medland.databinding.DialogCountryPhonePostalCodeBinding
import com.med.medland.presentation.fragment.otherComponents.PhoneMaskManager
import com.med.medland.presentation.fragment.otherComponents.adapter.SelectCountryCodeAdapter
import com.med.medland.presentation.fragment.otherComponents.model.PhoneMaskModel

class SelectCountryCodeDialog(
    private val context : Context,
    private val countryCodeAdapter : SelectCountryCodeAdapter,
    private val phoneMaskManager : PhoneMaskManager,
    private val codeShow : Boolean){

    private var dialog : AlertDialog? = null

    private lateinit var binding: DialogCountryPhonePostalCodeBinding


    private fun createDialog() {
        dialog = AlertDialog.Builder(context).create()
        binding = DialogCountryPhonePostalCodeBinding.inflate(dialog!!.layoutInflater)
        dialog!!.setView(binding.root)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun showDialog() {
        if (dialog == null) {
            createDialog()
        }
        binding.countryNumberRv.adapter = countryCodeAdapter
        countryCodeAdapter.filteredList(phoneMaskManager.loadPhoneMusk())

        if (!codeShow) binding.countryNumberSearch.hint = "Davlat nomi :"

        binding.countryNumberSearch.addTextChangedListener {

            if (isNumericToX(binding.countryNumberSearch.text.toString()))
                filterCountryCode("+${binding.countryNumberSearch.text.toString().replace("+","")}")
            else
                filterCountryName(binding.countryNumberSearch.text.toString())
        }

        binding.dialogCloseBtn.setOnClickListener {
            dialog!!.dismiss()
            dialog = null
        }
    }

    fun dismissDialog() {
        dialog!!.dismiss()
        dialog = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterCountryName(searchName : String) {
        val countryList = ArrayList<PhoneMaskModel>()
        for (i in phoneMaskManager.loadPhoneMusk()) {
            if (i.name.lowercase().contains(searchName.lowercase())) {
                countryList.add(i)
            }
        }
        countryCodeAdapter.filteredList(countryList)
    }

    private fun isNumericToX(toCheck: String): Boolean {
        return toCheck.toDoubleOrNull() != null
    }

    private fun filterCountryCode(code : String) {
        val countryList = ArrayList<PhoneMaskModel>()
        for (i in phoneMaskManager.loadPhoneMusk()) {
            if (i.countryCode.lowercase().contains(code)) {
                countryList.add(i)
            }
        }
        countryCodeAdapter.filteredList(countryList)
    }
}