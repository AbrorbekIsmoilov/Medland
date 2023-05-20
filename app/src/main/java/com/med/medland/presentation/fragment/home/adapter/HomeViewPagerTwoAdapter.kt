package com.med.medland.presentation.fragment.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.med.medland.R
import com.med.medland.databinding.ItemViewPagerTwoInHomeBinding
import com.med.medland.presentation.fragment.home.model.ViewPagerTwoModel

class HomeViewPagerTwoAdapter(private val itemList : List<ViewPagerTwoModel>,
                              val context: Context, val viewPager: ViewPager) :PagerAdapter(){

    class ViewHolder(private val binding: ItemViewPagerTwoInHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : ViewPagerTwoModel) {
            Glide.with(binding.root.context).load(item.image).into(binding.imageTitleInViewpagerItem)
            binding.tvTitleOneInViewpagerItem.text = item.title_one
            binding.tvTitleTwoInViewpagerItem.text = item.title_two
        }
    }


    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(container.context).inflate(R.layout.item_view_pager_two_in_home, null)
        container.addView(view,position)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View?)
    }

    override fun getCount() = itemList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}