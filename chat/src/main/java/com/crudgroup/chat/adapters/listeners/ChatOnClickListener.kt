package com.crudgroup.chat.adapters.listeners

import uz.crud.data.model.ChatData

interface ChatOnClickListener {
    fun onClick(item: ChatData)
}