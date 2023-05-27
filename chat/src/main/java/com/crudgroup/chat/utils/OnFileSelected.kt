package com.crudgroup.chat.utils

import android.net.Uri

interface OnFileSelected {

    fun onImageSelectedFromContent(imageUri: Uri?)
    fun onImageSelectedFromCamera(isSuccess: Boolean)
    fun onPdfSelectedFromContent(pdfUri: Uri?)
}