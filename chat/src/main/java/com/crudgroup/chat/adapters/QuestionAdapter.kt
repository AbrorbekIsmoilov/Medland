package com.crudgroup.chat.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.crudgroup.chat.adapters.holders.QuestionViewHolder
import com.crudgroup.chat.adapters.listeners.OnQuestionClickListener
import uz.crud.data.model.Question

class QuestionAdapter(
    private val onClickListener: OnQuestionClickListener
) : ListAdapter<Question, QuestionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.onBind(getItem(position).question, onClickListener)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Question>() {

        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }
    }
}