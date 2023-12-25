package com.example.codepiece.adapter

// QuestionAdapter.kt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.QuestionLayoutBinding

class QuestionAdapter(private val questionList: List<QuestionModel>) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(private val binding: QuestionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: QuestionModel) {
            // Bind the question data to the UI elements in the item layout
            binding.questionTextView.text =
                question.question  // Update this line based on your actual TextView ID
            binding.option1.text = question.option1
            binding.option2.text = question.option2
            binding.option3.text = question.option3
            binding.option4.text = question.option4
            // You need to add similar lines for other UI elements (e.g., RadioButtons) in your layout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding =
            QuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}