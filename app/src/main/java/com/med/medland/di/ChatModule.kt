package com.med.medland.di

import com.crudgroup.chat.multichoice.MultiChoiceHandler
import com.crudgroup.chat.multichoice.QuestionMultiChoiceHandler
import com.med.medland.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object ChatModule {

    @[AppScope Provides]
    fun provideMultiChoiceHandler(): MultiChoiceHandler {
        return QuestionMultiChoiceHandler()
    }
}