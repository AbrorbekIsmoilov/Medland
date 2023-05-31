package com.crudgroup.chat.adapters.listeners

interface OnMessageClickListener {

    fun onClick(adapterPosition: Int, message: String, id: String?)
}