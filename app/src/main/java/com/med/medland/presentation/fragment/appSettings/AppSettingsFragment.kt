package com.med.medland.presentation.fragment.appSettings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.med.medland.R
import com.med.medland.databinding.DialogNameEditBinding
import com.med.medland.databinding.FragmentAppSettingsBinding
import com.med.medland.databinding.FragmentMyProfileBinding
import com.med.medland.databinding.MyProfilNumberBinding
import com.med.medland.presentation.fragment.otherComponents.GetImageBottomSheet
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class AppSettingsFragment : Fragment() {

    private lateinit var binding : FragmentAppSettingsBinding
    private lateinit var getImageBottomSheet: GetImageBottomSheet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
            isGallery()
        editName()
        imageSelect()

        binding.exit.setOnClickListener {
            findNavController().navigate(R.id.action_appSettingsFragment_to_myProfileFragment)
        }


        return binding.root
    }
    fun isGallery() {

        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }
    }
    @SuppressLint("SimpleDateFormat")
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        it ?: return@registerForActivityResult
        binding.imageView.setImageURI(it)
        val inputStream = requireActivity().contentResolver.openInputStream(it)
        val vaqt = SimpleDateFormat("yyyyMMdd hh:mm:ss").format(Date())
        val file = File(requireActivity().filesDir, "$vaqt.jpg")
        val fileOutStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutStream)
        fileOutStream.close()
        inputStream?.close()
    }

    private fun editName(){
        val alertDialog = AlertDialog.Builder(requireContext())
        val dialogView = DialogNameEditBinding.inflate(layoutInflater)
        alertDialog.setView(dialogView.root)
        val dialog = alertDialog.create()
        binding.fullName.setOnClickListener {
            dialog.show()

            dialogView.btnFullName.setOnClickListener {
                binding.fullName.text =
                    "${dialogView.fullName.text} "
//                    findNavController().navigate(R.id.action_myProfileFragment_to_signUpThreeFragment2)
                Toast.makeText(context, "Bu oyna xali ishga tushmagan", Toast.LENGTH_SHORT).show()
                onDestroy()
//
                dialog.cancel()

            }

        }

        }

    private fun imageSelect(){
        binding.imageView.setOnClickListener {
            getImageBottomSheet = GetImageBottomSheet()
            getImageBottomSheet.setSelectedImage(binding.imageView)
            getImageBottomSheet.show(childFragmentManager, "Get Image")
        }
    }
}