package com.crudgroup.chat.utils

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.crudgroup.chat.utils.Constants.IMAGE_MEDIA_TYPE
import com.crudgroup.chat.utils.Constants.PDF_MEDIA_TYPE

class FilePicker(
    activityResultRegistry: ActivityResultRegistry?,
    lifecycleOwner: LifecycleOwner,
    private val onFileSelected: OnFileSelected
) {

    private val getContentForImage: ActivityResultLauncher<String>? =
        activityResultRegistry?.register(
            CONTENT_REGISTER_KEY_IMAGE,
            lifecycleOwner,
            ActivityResultContracts.GetContent(),
            onFileSelected::onImageSelectedFromContent
        )

    private val openCamera: ActivityResultLauncher<Uri?>? =
        activityResultRegistry?.register(
            CAMERA_REGISTER_KEY,
            lifecycleOwner,
            ActivityResultContracts.TakePicture(),
            onFileSelected::onImageSelectedFromCamera
        )

    private val getContentForPdf: ActivityResultLauncher<String>? =
        activityResultRegistry?.register(
            CONTENT_REGISTER_KEY_PDF,
            lifecycleOwner,
            ActivityResultContracts.GetContent(),
            onFileSelected::onPdfSelectedFromContent
        )

    fun pickImageFromContent() {
        getContentForImage?.launch(IMAGE_MEDIA_TYPE)
    }

    fun pickImageFromCamera(input: Uri?) {
        openCamera?.launch(input)
    }

    fun pickPdfFromContent() {
        getContentForPdf?.launch(PDF_MEDIA_TYPE)
    }

    companion object {
        private const val CAMERA_REGISTER_KEY = "camera"
        private const val CONTENT_REGISTER_KEY_IMAGE = "content_image"
        private const val CONTENT_REGISTER_KEY_PDF = "content_pdf"
    }
}