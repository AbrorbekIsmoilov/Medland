package com.med.medland.presentation.fragment.ordersFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.med.medland.R
import com.med.medland.databinding.FragmentOrdersBinding
import com.med.medland.presentation.fragment.ordersFragment.adapter.OrdersAdapter


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ordersRv.adapter = OrdersAdapter()
    }
}