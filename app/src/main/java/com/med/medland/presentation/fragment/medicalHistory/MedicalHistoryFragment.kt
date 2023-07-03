package com.med.medland.presentation.fragment.medicalHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.FragmentMedicalHistoryBinding

class MedicalHistoryFragment : Fragment() {
        private val binding by lazy { FragmentMedicalHistoryBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
exitClick()





        return binding.root
    }

    private fun exitClick(){

        binding.exit.setOnClickListener {
            findNavController().navigate(R.id.action_medicalHistoryFragment_to_myProfileFragment)
        }
    }
}