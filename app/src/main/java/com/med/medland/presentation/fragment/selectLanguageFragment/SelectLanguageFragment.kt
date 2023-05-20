package com.med.medland.presentation.fragment.selectLanguageFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.data.locale.Constants
import com.med.medland.databinding.FragmentSelectLanguageBinding
import com.orhanobut.hawk.Hawk


class SelectLanguageFragment : Fragment() {

    private lateinit var binding: FragmentSelectLanguageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.languageEn.setOnClickListener {
            Hawk.put(Constants.SELECT_LANGUAGE, Constants.EN_LANGUAGE)
            goNextPage()
        }
        binding.languageRu.setOnClickListener {
            Hawk.put(Constants.SELECT_LANGUAGE, Constants.RU_LANGUAGE)
            goNextPage()
        }
        binding.languageUz.setOnClickListener {
            Hawk.put(Constants.SELECT_LANGUAGE, Constants.UZ_LANGUAGE)
            goNextPage()
        }
    }

    private fun goNextPage() {
        if (Hawk.get<Int>(Constants.WATCH_INTERVIEW) == Constants.WATCH_ENDED)
            findNavController().navigate(R.id.action_selectLanguageFragment_to_loginFragment)
        else findNavController().navigate(R.id.action_selectLanguageFragment_to_interViewFragment)
    }
}