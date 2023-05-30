package com.med.medland.presentation.fragment.diagnosticFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.med.medland.R
import com.med.medland.databinding.FragmentDiagnosticOrderBinding


class DiagnosticOrderFragment : Fragment() {

    private lateinit var binding: FragmentDiagnosticOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDiagnosticOrderBinding.inflate(inflater, container, false)
        return binding.root
    }
}