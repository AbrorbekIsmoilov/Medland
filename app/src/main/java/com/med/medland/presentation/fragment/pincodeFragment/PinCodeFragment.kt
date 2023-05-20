package com.med.medland.presentation.fragment.pincodeFragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.FragmentPinCodeBinding


class PinCodeFragment : Fragment() {

    private lateinit var binding: FragmentPinCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPinCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        object : CountDownTimer(5000, 5000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                findNavController().navigate(R.id.action_pinCodeFragment_to_congratulationsFragment)
            }
        }
    }
}