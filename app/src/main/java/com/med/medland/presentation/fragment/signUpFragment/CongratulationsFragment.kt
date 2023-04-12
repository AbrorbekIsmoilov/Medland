package com.med.medland.presentation.fragment.signUpFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.FragmentCongratulationsBinding


class CongratulationsFragment : Fragment() {

    private lateinit var binding: FragmentCongratulationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCongratulationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.congratulationsHomeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_congratulationsFragment_to_patient_nav_graph)
        }
    }
}