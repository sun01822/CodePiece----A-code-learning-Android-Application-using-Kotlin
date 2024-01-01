package com.example.codepiece.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.databinding.AdminQuestionLayoutBinding
import com.example.codepiece.data.QuestionModel

class AdminQuestionAdapter(
    private val questionList: List<QuestionModel>,
    private val onLongPressListener: (Int) -> Unit,
    private val onPressListener: (Int) -> Unit
) : RecyclerView.Adapter<AdminQuestionAdapter.AdminQuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminQuestionViewHolder {
        val binding = AdminQuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminQuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminQuestionViewHolder, position: Int) {
        val question = questionList[position]

        // Set question title
        holder.binding.questionTextView.text = question.question

        // Set options
        holder.binding.option1.text = question.option1
        holder.binding.option2.text = question.option2
        holder.binding.option3.text = question.option3
        holder.binding.option4.text = question.option4
        holder.binding.answer.text = "Answer: " + question.answer

        // Handle long press
        holder.itemView.setOnLongClickListener {
            onLongPressListener.invoke(position)
            true
        }
        // Handle press
        holder.itemView.setOnClickListener {
            onPressListener.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class AdminQuestionViewHolder(val binding: AdminQuestionLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}
