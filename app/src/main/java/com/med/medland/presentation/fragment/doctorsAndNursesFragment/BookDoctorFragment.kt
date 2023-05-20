package com.med.medland.presentation.fragment.doctorsAndNursesFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.med.medland.R
import com.med.medland.databinding.FragmentBookDoctorBinding


class BookDoctorFragment : Fragment() {

    private lateinit var binding : FragmentBookDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBookDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }
}