package com.med.medland.presentation.fragment.otherComponents

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.med.medland.R


class MyCustomSnackBar(val view : View, layoutInflater: LayoutInflater) {


    @SuppressLint("InflateParams")
    private val customSnackView: View = layoutInflater.inflate(R.layout.snackbar_message, null)
    private val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    init {
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        snackBarLayout.setPadding(0, 80, 0, 0);
        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBar.view.layoutParams = params
        snackBarLayout.addView(customSnackView, 0)
    }


    fun showErrorSnack(message : String) {
        customSnackView.findViewById<TextView>(R.id.snackbar_message).text = message
        snackBar.show()
    }
    fun showAlertSnack(message : String) {
        customSnackView.findViewById<TextView>(R.id.snackbar_message).text = message
        customSnackView.findViewById<CardView>(R.id.snackbar_root).setCardBackgroundColor(view.resources.getColor(R.color.blue_color))
        snackBar.show()
    }

}