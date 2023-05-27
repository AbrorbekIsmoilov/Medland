package com.med.medland.presentation.app

import android.app.Application
import com.crudgroup.chat.di.ChatDepsStore
import com.med.medland.di.AppComponent
import com.med.medland.di.DaggerAppComponent
import com.orhanobut.hawk.Hawk

class ApplicationController : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(application = this)
            .context(context = applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        ChatDepsStore.deps = appComponent
        Hawk.init(this).build()
    }
}