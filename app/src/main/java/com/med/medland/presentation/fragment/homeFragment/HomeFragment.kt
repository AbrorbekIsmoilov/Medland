package com.med.medland.presentation.fragment.homeFragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.med.medland.R
import com.med.medland.databinding.FragmentHomeBinding
import com.med.medland.presentation.fragment.homeFragment.adapter.DiscountsViewPagerAdapter
import com.med.medland.presentation.fragment.homeFragment.adapter.DoctorsAdapter
import com.med.medland.presentation.fragment.homeFragment.adapter.HomeViewPagerTwoAdapter
import com.med.medland.presentation.fragment.homeFragment.model.DiscountModel
import com.med.medland.presentation.fragment.homeFragment.model.DoctorsModel
import com.med.medland.presentation.fragment.homeFragment.model.ViewPagerTwoModel
import com.med.medland.presentation.fragment.otherComponents.MyCustomIndicator
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myCustomIndicator: MyCustomIndicator
    private var discountsList : ArrayList<DiscountModel>? = null
    private var itemList : ArrayList<ViewPagerTwoModel>? = null
    private var doctorsList : ArrayList<DoctorsModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myCustomIndicator = MyCustomIndicator(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.discountsViewpager.adapter = getDiscountsAdapter()
//        binding.homeViewpagerTwo.adapter = getViewPagerTwoItem()
        binding.rvDoctorsInCategories.adapter = getDoctorsAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        myCustomIndicator.setUpIndicators(getDiscountsAdapter().itemCount, binding.homeIndicatorLayoutOne, Color.BLACK)
//        myCustomIndicator.setUpCurrentIndicator(0, binding.homeIndicatorLayoutOne, Color.BLACK)
        myCustomIndicator.setUpIndicators(getDiscountsAdapter().itemCount, binding.homeIndicatorLayoutTwo, Color.GREEN)
        myCustomIndicator.setUpCurrentIndicator(0, binding.homeIndicatorLayoutTwo, Color.GREEN)
    }

    private fun getDiscountsAdapter() : DiscountsViewPagerAdapter {
        discountsList = ArrayList()
        discountsList?.add(DiscountModel("https://t3.ftcdn.net/jpg/02/95/51/80/360_F_295518052_aO5d9CqRhPnjlNDTRDjKLZHNftqfsxzI.jpg"))
        discountsList?.add(DiscountModel("https://media.istockphoto.com/id/1200980392/photo/medical-concept-of-asian-beautiful-female-doctor-in-white-coat-with-stethoscope-waist-up.jpg?s=612x612&w=0&k=20&c=nD_1Tn11kWcMZwZfnyA-lYAvNKcBeoEK_KLObBnN6Jg="))
        discountsList?.add(DiscountModel("https://images.newindianexpress.com/uploads/user/imagelibrary/2021/10/1/w900X450/TS_rural_hosps-.jpg?w=400&dpr=2.6"))

        return DiscountsViewPagerAdapter(discountsList!!)
    }

    private fun getViewPagerTwoItem() : HomeViewPagerTwoAdapter {
        itemList = ArrayList()
        itemList?.add(ViewPagerTwoModel("Emergency medical care instruction","Lorem ipsum dolor sit amet, consectetur adipiscing elit.",R.drawable.health_plus))
        itemList?.add(ViewPagerTwoModel("Emergency medical care instruction","Lorem ipsum dolor sit amet, consectetur adipiscing elit.",R.drawable.avocado))
        itemList?.add(ViewPagerTwoModel("Emergency medical care instruction","Lorem ipsum dolor sit amet, consectetur adipiscing elit.",R.drawable.recipe_book))

        return HomeViewPagerTwoAdapter(itemList!!)
    }

    private fun getDoctorsAdapter() : DoctorsAdapter {
        doctorsList = ArrayList()
        doctorsList?.add(DoctorsModel(R.drawable.doctor_image_one,"Izzatillo Umarov","Online"))
        doctorsList?.add(DoctorsModel(R.drawable.doctor_image_two,"Azamat Botirov","Online"))
        doctorsList?.add(DoctorsModel(R.drawable.doctor_image_three,"Sherali Jo'rayev","Online"))
        return DoctorsAdapter(doctorsList!!)
    }
}