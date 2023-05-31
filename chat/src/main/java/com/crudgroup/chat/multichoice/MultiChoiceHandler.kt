package com.crudgroup.chat.multichoice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.crud.data.model.Question

interface MultiChoiceHandler {

    fun setItemsFlow(coroutineScope: CoroutineScope, itemsFlow: Flow<List<Question>>)

    fun listen(): Flow<MultiChoiceState>

    fun toggle(item: Question)

    fun check(item: Question)

    fun clear(item: Question)
}