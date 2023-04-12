package com.med.medland.presentation.fragment.otherComponents

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.med.medland.R

class MyCustomIndicator(val context : Context) {

    fun setUpIndicators(indicatorCount : Int, indicatorLayout: LinearLayout, color: Int) {
        val indicators = arrayOfNulls<ImageView>(indicatorCount)
        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8,0,8,0)
        for ( i in indicators.indices) {
            indicators[i] = ImageView(context)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_not_active))
            indicators[i]?.setColorFilter(color)
            indicators[i]?.layoutParams = layoutParams
            indicatorLayout.addView(indicators[i])
        }
    }

    fun setUpCurrentIndicator(index : Int, indicatorLayout: LinearLayout, color : Int) {
        val childCount = indicatorLayout.childCount
        for ( i in 0 until childCount){
            val imageView = indicatorLayout[i] as ImageView
            if( i == index) imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_active))
            else imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_not_active))

            imageView.setColorFilter(color)
        }
    }

}