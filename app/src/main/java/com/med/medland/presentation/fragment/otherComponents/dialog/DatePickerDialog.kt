package com.med.medland.presentation.fragment.otherComponents.dialog


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.med.medland.databinding.DatePickerDialogBinding
import com.ozcanalasalvar.library.utils.DateUtils
import com.ozcanalasalvar.library.view.datePicker.DatePicker


class DatePickerDialog(val context : Context) {

    private var dialog : Dialog? = null
    private lateinit var binding: DatePickerDialogBinding
    private var selectedDate : String = ""

    private fun createDialog() {
        dialog = Dialog(context)
        binding = DatePickerDialogBinding.inflate(dialog!!.layoutInflater)
        dialog!!.setContentView(binding.root)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showCalendarDialog(tv_date: TextView) {
        if (dialog == null) {
            createDialog()
        }

        binding.datePicker.offset = 3
        binding.datePicker.setTextSize(19)
        binding.datePicker.setPickerMode(DatePicker.DAY_ON_FIRST)
        binding.datePicker.minDate = DateUtils.getTimeMiles(1950, 1, 1)
        binding.datePicker.maxDate = DateUtils.getTimeMiles(2018, 12, 31)
        binding.datePicker.date = DateUtils.getTimeMiles(2000, 8, 8)


        binding.cancelBtn.setOnClickListener {
            dismissDialog()
        }

        binding.datePicker.setDataSelectListener { date, day, month, year ->
            selectedDate = "$day / ${month.inc()} / $year"
        }

        binding.setDateBtn.setOnClickListener {
            tv_date.text = selectedDate
            dismissDialog()
        }
    }
    fun showCalendarDialogs(tv_date: TextView) {
        if (dialog == null) {
            createDialog()
        }

        binding.datePicker.offset = 3
        binding.datePicker.setTextSize(19)
        binding.datePicker.setPickerMode(DatePicker.DAY_ON_FIRST)
        binding.datePicker.minDate = DateUtils.getTimeMiles(1950, 1, 1)
        binding.datePicker.maxDate = DateUtils.getTimeMiles(2018, 12, 31)
        binding.datePicker.date = DateUtils.getTimeMiles(2000, 8, 8)


        binding.cancelBtn.setOnClickListener {
            dismissDialog()
        }

        binding.datePicker.setDataSelectListener { date, day, month, year ->
            selectedDate = "$day.${month.inc()}.$year"
        }

        binding.setDateBtn.setOnClickListener {
            tv_date.text = selectedDate
            dismissDialog()
        }
    }

    private fun dismissDialog() {
        dialog!!.dismiss()
        dialog = null
    }

}