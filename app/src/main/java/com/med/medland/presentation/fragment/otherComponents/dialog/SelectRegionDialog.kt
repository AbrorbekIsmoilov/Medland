package com.med.medland.presentation.fragment.otherComponents.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.med.medland.databinding.DialogSelectRegionAndDistrictBinding
import com.med.medland.presentation.fragment.otherComponents.model.GetRegionModel

class SelectRegionDialog(val context: Context, private val selectRegionListener: SelectRegionListener) {

    private var dialog : AlertDialog? = null

    private lateinit var binding: DialogSelectRegionAndDistrictBinding

    interface SelectRegionListener {
        fun selectedRegionIdAndDistrictId(regionName : String, regionId : Int, districtName : String, districtId : Int)
    }

    private fun createDialog() {
        dialog = AlertDialog.Builder(context).create()
        binding = DialogSelectRegionAndDistrictBinding.inflate(dialog!!.layoutInflater)
        dialog!!.setView(binding.root)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun showDialog(getRegionList : ArrayList<GetRegionModel>) {
        if (dialog == null) { createDialog() }
        val regionList = ArrayList<String>()
        val districtList = ArrayList<String>()

        for (i in getRegionList) { regionList.add(i.nomi) }
        for (i in getRegionList[0].tuman_viloyat) { districtList.add(i.nomi) }

        val regionAdapter = ArrayAdapter(context, com.med.medland.R.layout.item_spinner_text, regionList)
        regionAdapter.setDropDownViewResource(com.med.medland.R.layout.item_spinner_dropdown_list)
        val districtAdapter = ArrayAdapter(context, com.med.medland.R.layout.item_spinner_text, districtList)
        districtAdapter.setDropDownViewResource(com.med.medland.R.layout.item_spinner_dropdown_list)

        binding.dialogSelectRegionSpinner.adapter = regionAdapter
        binding.dialogSelectDistrictSpinner.adapter = districtAdapter

        binding.dialogSelectRegionSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                districtList.clear()
                for (i in getRegionList[position].tuman_viloyat) { districtList.add(i.nomi) }
                districtAdapter.notifyDataSetChanged()
                binding.dialogSelectDistrictSpinner.adapter = districtAdapter
            }
        }

        binding.selectedRegionAndDistrictBtn.setOnClickListener {
            val selectedRegionPosition = binding.dialogSelectRegionSpinner.selectedItemPosition
            val selectedDistrictPosition = binding.dialogSelectDistrictSpinner.selectedItemPosition
            val selectedRegionName = getRegionList[selectedRegionPosition].nomi
            val selectedRegionId = getRegionList[selectedRegionPosition].id
            val selectedDistrictName = getRegionList[selectedRegionPosition].tuman_viloyat[selectedDistrictPosition].nomi
            val selectedDistrictId = getRegionList[selectedRegionPosition].tuman_viloyat[selectedDistrictPosition].id
            selectRegionListener.selectedRegionIdAndDistrictId(
                selectedRegionName, selectedRegionId, selectedDistrictName,selectedDistrictId)

            dialog!!.dismiss()
            dialog = null
        }

        binding.getRegionDialogDismissBtn.setOnClickListener {
            dialog!!.dismiss()
            dialog = null
        }
    }
}