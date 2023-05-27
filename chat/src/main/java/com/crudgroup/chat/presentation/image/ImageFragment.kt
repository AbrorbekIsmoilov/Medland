package com.crudgroup.chat.presentation.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.crudgroup.chat.utils.Constants.CHAT_IMAGE
import com.example.chat.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUri: String? = arguments?.getString(CHAT_IMAGE)
        binding.chatImage.load(imageUri) {
            crossfade(true)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}