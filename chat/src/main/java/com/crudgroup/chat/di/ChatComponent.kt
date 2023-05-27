package com.crudgroup.chat.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import com.crudgroup.chat.Chat
import com.crudgroup.chat.multichoice.MultiChoiceHandler
import com.crudgroup.chat.presentation.chat.ChatFragment
import dagger.Component
import uz.crud.data.repository.ChatRepository
import kotlin.properties.Delegates.notNull

@[Chat Component(dependencies = [ChatDeps::class])]
internal interface ChatComponent {

    fun inject(fragment: ChatFragment)

    @Component.Builder
    interface Builder {

        fun deps(chatDeps: ChatDeps): Builder

        fun build(): ChatComponent
    }
}

interface ChatDeps {
    val chatRepository: ChatRepository
    val multiChoiceHandler: MultiChoiceHandler
}

interface ChatDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: ChatDeps

    companion object : ChatDepsProvider by ChatDepsStore
}

object ChatDepsStore : ChatDepsProvider {

    override var deps: ChatDeps by notNull()
}

internal class ChatComponentViewModel : ViewModel() {

    val chatComponent =
        DaggerChatComponent.builder().deps(ChatDepsProvider.deps).build()
}