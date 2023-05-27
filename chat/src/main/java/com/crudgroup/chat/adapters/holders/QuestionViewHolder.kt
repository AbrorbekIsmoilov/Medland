package com.crudgroup.chat.adapters.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.crudgroup.chat.adapters.listeners.OnQuestionClickListener
import com.example.chat.databinding.ItemQuestionBinding

class QuestionViewHolder(
    private val itemBinding: ItemQuestionBinding
) : ViewHolder(itemBinding.root) {

    fun onBind(question: String, onClickListener: OnQuestionClickListener) {
        itemBinding.questionTextTv.text = question
        itemBinding.root.setOnClickListener {
            onClickListener.onClick(question)
        }
    }

    companion object {
        fun create(viewGroup: ViewGroup): QuestionViewHolder {
            return QuestionViewHolder(
                ItemQuestionBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }
}