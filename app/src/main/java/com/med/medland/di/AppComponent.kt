package com.med.medland.di

import android.app.Application
import android.content.Context
import com.crudgroup.chat.di.ChatDeps
import com.crudgroup.chat.multichoice.MultiChoiceHandler
import com.med.medland.presentation.fragment.chat.ChatFragment
import dagger.BindsInstance
import dagger.Component
import uz.crud.data.repository.ChatRepository
import javax.inject.Scope

@[AppScope Component(modules = [DataModule::class, NetworkModule::class, ChatModule::class, RepositoryModule::class])]
interface AppComponent : ChatDeps {

    override val chatRepository: ChatRepository
    override val multiChoiceHandler: MultiChoiceHandler

    fun inject(chatFragment: ChatFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}

@Scope
annotation class AppScope