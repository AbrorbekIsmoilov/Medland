package com.med.medland.presentation.fragment.ordersFragment.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.med.medland.databinding.ItemOrderUpcomingBinding
import com.med.medland.presentation.fragment.otherComponents.SetCustomization.margin

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrderUpcomingHolder>() {


    private var upcomingOrder = 1


    inner class OrderUpcomingHolder(val binding: ItemOrderUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(itemCount : Int) {
            if (itemCount == 3) {
                itemView.margin(20F,10F,20F,20F)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderUpcomingHolder {
        return OrderUpcomingHolder(ItemOrderUpcomingBinding.inflate(LayoutInflater.from(parent.context),parent, false))

    }

    override fun getItemCount() = 4

    override fun onBindViewHolder(holder: OrderUpcomingHolder, position: Int) {
        holder.onBind(position)
    }

}