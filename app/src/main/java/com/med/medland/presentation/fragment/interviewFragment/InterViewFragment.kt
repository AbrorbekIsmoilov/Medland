package com.med.medland.presentation.fragment.interviewFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.med.medland.R
import com.med.medland.databinding.FragmentInterViewBinding
import com.med.medland.presentation.fragment.interviewFragment.adapter.InterviewAdapter
import com.med.medland.presentation.fragment.interviewFragment.model.InterviewItemModel
import java.util.ArrayList


class InterViewFragment : Fragment() {

    private lateinit var binding: FragmentInterViewBinding
    private var interViewList : ArrayList<InterviewItemModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInterViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.interviewViewpager.adapter = getInterviewAdapter()
        setUpIndicators()
        setUpCurrentIndicator(0)

        binding.interviewViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setUpCurrentIndicator(position)
                binding.interviewTitle.text = interViewList!![position].title
            }
        })

        binding.interviewNextBtn.setOnClickListener {
            val interviewPosition = binding.interviewViewpager.currentItem
            if ( interviewPosition == getInterviewAdapter().itemCount.dec()){
                findNavController().navigate(R.id.action_interViewFragment_to_loginFragment)
            }
            else binding.interviewViewpager.currentItem = interviewPosition.inc()
        }
    }

    private fun getInterviewAdapter() : InterviewAdapter {
        interViewList = ArrayList()
        interViewList?.add(InterviewItemModel("https://t3.ftcdn.net/jpg/02/95/51/80/360_F_295518052_aO5d9CqRhPnjlNDTRDjKLZHNftqfsxzI.jpg","Eng yaxshi doktorlar"))
        interViewList?.add(InterviewItemModel("https://media.istockphoto.com/id/1200980392/photo/medical-concept-of-asian-beautiful-female-doctor-in-white-coat-with-stethoscope-waist-up.jpg?s=612x612&w=0&k=20&c=nD_1Tn11kWcMZwZfnyA-lYAvNKcBeoEK_KLObBnN6Jg=","Eng yaxshi hamshiralar"))
        interViewList?.add(InterviewItemModel("https://images.newindianexpress.com/uploads/user/imagelibrary/2021/10/1/w900X450/TS_rural_hosps-.jpg?w=400&dpr=2.6","Online diagnostika"))

        return InterviewAdapter(interViewList!!)
    }

    private fun setUpIndicators() {
        val indicators = arrayOfNulls<ImageView>(getInterviewAdapter().itemCount)
        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for ( i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_not_active))
            indicators[i]?.layoutParams = layoutParams
            binding.interviewIndicatorLayout.addView(indicators[i])
        }
    }

    private fun setUpCurrentIndicator(index : Int) {
        val childCount = binding.interviewIndicatorLayout.childCount
        for ( i in 0 until childCount){
            val imageView = binding.interviewIndicatorLayout[i] as ImageView
            if( i == index) imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_active))
            else imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_not_active))
        }
    }

}