package com.med.medland.presentation.fragment.interviewFragment

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.med.medland.R
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentInterViewBinding
import com.med.medland.presentation.fragment.interviewFragment.adapter.InterviewAdapter
import com.med.medland.presentation.fragment.interviewFragment.model.InterviewItemModel
import com.med.medland.presentation.fragment.otherComponents.MyCustomIndicator
import com.orhanobut.hawk.Hawk
import java.util.ArrayList


class InterViewFragment : Fragment() {

    private lateinit var binding: FragmentInterViewBinding
    private var interViewList : ArrayList<InterviewItemModel>? = null
    private lateinit var myCustomIndicator: MyCustomIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myCustomIndicator = MyCustomIndicator(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInterViewBinding.inflate(inflater, container, false)

        binding.interviewNextBtn.setTint(4, 90, 253)
        binding.interviewNextBtn.rectWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 150f,
            requireContext().resources.displayMetrics
        )
        binding.interviewNextBtn.rectHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 60f,
            requireContext().resources.displayMetrics
        )
        binding.interviewNextBtn.roundRadius = 0f

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.interviewViewpager.adapter = getInterviewAdapter()
        myCustomIndicator.setUpIndicators(getInterviewAdapter().itemCount, binding.interviewIndicatorLayout, resources.getColor(R.color.gender_gray))
        myCustomIndicator.setUpCurrentIndicator(0, binding.interviewIndicatorLayout, resources.getColor(R.color.blue_color))

        binding.interviewViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                myCustomIndicator.setUpCurrentIndicator(position, binding.interviewIndicatorLayout, resources.getColor(R.color.blue_color))
                binding.interviewTitle.text = interViewList!![position].title
            }
        })

        binding.interviewNextBtn.setOnClickListener {
            val interviewPosition = binding.interviewViewpager.currentItem
            if ( interviewPosition == getInterviewAdapter().itemCount.dec()){
                Hawk.put(Constants.WATCH_INTERVIEW, Constants.WATCH_ENDED)
                findNavController().navigate(R.id.action_interViewFragment_to_loginFragment)
            }
            else binding.interviewViewpager.currentItem = interviewPosition.inc()
        }
    }

    private fun getInterviewAdapter() : InterviewAdapter {
        interViewList = ArrayList()
        interViewList?.add(InterviewItemModel(R.drawable.interview_one_image,"Eng yaxshi doktorlar"))
        interViewList?.add(InterviewItemModel(R.drawable.interview_two_image,"Eng yaxshi hamshiralar"))
        interViewList?.add(InterviewItemModel(R.drawable.interview_three_image,"Online diagnostika"))

        return InterviewAdapter(interViewList!!)
    }

}