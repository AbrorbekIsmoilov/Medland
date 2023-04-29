package com.med.medland.presentation.app

import android.app.Application
import com.orhanobut.hawk.Hawk

class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
    }
}