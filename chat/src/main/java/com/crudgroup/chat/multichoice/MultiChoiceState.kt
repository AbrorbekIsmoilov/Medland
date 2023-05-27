package com.crudgroup.chat.multichoice

import uz.crud.data.model.Question

interface MultiChoiceState {

    fun isChecked(item: Question): Boolean
}