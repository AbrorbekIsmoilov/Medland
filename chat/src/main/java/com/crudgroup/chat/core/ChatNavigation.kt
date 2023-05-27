package com.crudgroup.chat.core

import android.os.Bundle

interface ChatNavigation {
    fun navigateToVideoChatFragment(arguments: Bundle?)
    fun onBackPressed()
    fun navigateToDoctorDetail(arguments: Bundle?)
}