package com.med.medland.presentation.fragment.patientFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.med.medland.R
import com.med.medland.databinding.FragmentPatientBinding

class PatientFragment : Fragment() {

    private lateinit var binding: FragmentPatientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPatientBinding.inflate(inflater, container, false)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.patient_container) as NavHostFragment
        NavigationUI.setupWithNavController(binding.patientBottomNav, navHostFragment.navController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}