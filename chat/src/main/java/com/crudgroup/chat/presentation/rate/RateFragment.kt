package com.crudgroup.chat.presentation.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.chat.databinding.FragmentRateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RateFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRateBinding? = null
    private val binding: FragmentRateBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments
        if (data != null) {
            with(binding) {
                avatarIv.load(data.getString("image"))
                fullNameTv.text = data.getString("name")
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            RateFragment().apply {
                arguments = bundle
            }
    }
}