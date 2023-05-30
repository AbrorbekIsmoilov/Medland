package com.med.medland.presentation.fragment.diagnosticFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.med.medland.databinding.FragmentAllDiagnosticsBinding


class AllDiagnosticsFragment : Fragment() {

    private lateinit var binding: FragmentAllDiagnosticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAllDiagnosticsBinding.inflate(inflater, container,false)
        return binding.root
    }
}