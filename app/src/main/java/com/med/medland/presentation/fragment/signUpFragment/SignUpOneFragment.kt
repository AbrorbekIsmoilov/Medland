package com.med.medland.presentation.fragment.signUpFragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.FragmentSignUpOneBinding
import com.med.medland.presentation.fragment.otherComponents.dialog.DatePickerDialog
import com.med.medland.presentation.fragment.otherComponents.GetImageBottomSheet


class SignUpOneFragment : Fragment() {

    private lateinit var binding : FragmentSignUpOneBinding
    private var genMan = true
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var getImageBottomSheet: GetImageBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datePickerDialog = DatePickerDialog(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpOneBinding.inflate(inflater, container, false)
        binding.signUpSelectedImage.setImageResource(R.drawable.carton_man_image_one)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signUpChangeImageBtn.setOnClickListener {
            getImageBottomSheet = GetImageBottomSheet()
            getImageBottomSheet.setSelectedImage(binding.signUpSelectedImage)
            getImageBottomSheet.show(childFragmentManager, "Get Image")
        }

        binding.signUpGenManBtn.setOnClickListener {
            if (!genMan) {
                binding.signUpGenManBtn.setBackgroundResource(R.drawable.gender_blue_background)
                binding.signUpGenGirlBtn.setBackgroundResource(R.drawable.gender_gray_background)
                binding.signUpGenManBtn.setTextColor(Color.WHITE)
                binding.signUpGenGirlBtn.setTextColor(Color.BLACK)
                genMan = true
            }
        }

        binding.signUpGenGirlBtn.setOnClickListener {
            if (genMan) {
                binding.signUpGenGirlBtn.setBackgroundResource(R.drawable.gender_blue_background)
                binding.signUpGenManBtn.setBackgroundResource(R.drawable.gender_gray_background)
                binding.signUpGenManBtn.setTextColor(Color.BLACK)
                binding.signUpGenGirlBtn.setTextColor(Color.WHITE)
                genMan = false
            }
        }

        binding.signUpOneNextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpOneFragment_to_signUpTwoFragment)
        }


        binding.etBrithDate.setOnClickListener {
            datePickerDialog.showCalendarDialog(binding.etBrithDate)
        }

        binding.signUpSignInBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}