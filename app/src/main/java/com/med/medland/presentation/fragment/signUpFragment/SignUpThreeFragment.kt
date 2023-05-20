package com.med.medland.presentation.fragment.signUpFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.FragmentSignUpThreeBinding


class SignUpThreeFragment : Fragment() {

    private lateinit var binding: FragmentSignUpThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpSignInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpThreeFragment_to_loginFragment)
        }

        binding.signUpThreeNextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpThreeFragment_to_pinCodeFragment)
        }

        binding.verificationCodeOne.addTextChangedListener {
            if (binding.verificationCodeOne.text.toString().isNotEmpty()) {
                binding.verificationCodeTwo.text.clear()
                binding.verificationCodeTwo.requestFocus()
            }
        }
        binding.verificationCodeTwo.addTextChangedListener {
            if (binding.verificationCodeTwo.text.toString().isNotEmpty()) {
                binding.verificationCodeThree.text.clear()
                binding.verificationCodeThree.requestFocus()
            }
        }
        binding.verificationCodeThree.addTextChangedListener {
            if (binding.verificationCodeThree.text.toString().isNotEmpty()) {
                binding.verificationCodeFour.text.clear()
                binding.verificationCodeFour.requestFocus()
            }
        }
    }
}