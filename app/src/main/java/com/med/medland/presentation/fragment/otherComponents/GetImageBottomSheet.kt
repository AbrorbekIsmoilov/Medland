package com.med.medland.presentation.fragment.otherComponents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.med.medland.R
import com.med.medland.databinding.GetImageLayoutBinding
import java.io.File
import java.io.FileOutputStream

class GetImageBottomSheet : BottomSheetDialogFragment() {

    private var image : ImageView? = null
    private lateinit var binding: GetImageLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = GetImageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setSelectedImage(image : ImageView) {
        this.image = image
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.manImageOne.setOnClickListener {
            image?.setImageResource(R.drawable.carton_man_image_one)
            dismiss()
        }
        binding.manImageTwo.setOnClickListener {
            image?.setImageResource(R.drawable.carton_man_image_two)
            dismiss()
        }
        binding.manImageThree.setOnClickListener {
            image?.setImageResource(R.drawable.carton_man_image_three)
            dismiss()
        }
        binding.girlImageOne.setOnClickListener {
            image?.setImageResource(R.drawable.carton_girl_image_one)
            dismiss()
        }
        binding.girlImageTwo.setOnClickListener {
            image?.setImageResource(R.drawable.carton_girl_image_two)
            dismiss()
        }
        binding.girlImageThree.setOnClickListener {
            image?.setImageResource(R.drawable.carton_girl_image_three)
            dismiss()
        }

        binding.imageGetFromCameraBtn.setOnClickListener {

            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .createIntent { startForProfileImageResult.launch(it) }
        }

        binding.imageGetFromGalleryBtn.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()	//User can only select image from Gallery
                .crop()
                .createIntent { startForProfileImageResult.launch(it) }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri: Uri = data?.data!!
                    image?.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}