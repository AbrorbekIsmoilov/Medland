package com.crudgroup.chat.multichoice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.crud.data.model.Question

class QuestionMultiChoiceHandler : MultiChoiceHandler, MultiChoiceState {

    private val checkedIds = HashSet<String>()
    private var items: List<Question> = emptyList()
    private val stateFlow = MutableStateFlow(Any())

    override fun setItemsFlow(coroutineScope: CoroutineScope, itemsFlow: Flow<List<Question>>) {
        coroutineScope.launch {
            itemsFlow.collectLatest { questions: List<Question> ->
                items = questions
                removeDeletedQuestion(questions)
                notifyUpdates()
            }
        }
    }

    override fun listen(): Flow<MultiChoiceState> {
        return stateFlow.map { this }
    }

    override fun clear(item: Question) {
        if (!exists(item)) return

        checkedIds.remove(item.question)

        notifyUpdates()
    }

    override fun check(item: Question) {
        if (!exists(item)) return

        checkedIds.add(item.question)

        notifyUpdates()
    }

    override fun toggle(item: Question) {
        if (isChecked(item)) {
            clear(item)
        } else {
            check(item)
        }
    }

    override fun isChecked(item: Question): Boolean {
        return checkedIds.contains(item.question)
    }

    private fun exists(item: Question): Boolean {
        return items.any { it.question == item.question }
    }

    private fun removeDeletedQuestion(questions: List<Question>) {
        val existingQuestions = HashSet(questions.map { it.question })
        val iterator = checkedIds.iterator()
        while (iterator.hasNext()) {
            val question = iterator.next()
            if (!existingQuestions.contains(question)) {
                iterator.remove()
            }
        }
    }

    private fun notifyUpdates() {
        stateFlow.value = Any()
    }
}