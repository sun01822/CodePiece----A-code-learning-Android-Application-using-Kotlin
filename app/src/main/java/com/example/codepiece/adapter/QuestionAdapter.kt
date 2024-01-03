package com.example.codepiece.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.QuestionLayoutBinding

class QuestionAdapter(
    private val questionList: List<QuestionModel>,
    private var onOptionSelectedListener: (Int, String?) -> Unit,
    private var selectedItemsCountListener: (Int) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    private val selectedAnswers = mutableMapOf<Int, String?>()
    private var selectedItemsCount = 0

    inner class QuestionViewHolder(private val binding: QuestionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set up listeners for radio buttons
            binding.option1.setOnClickListener { onOptionSelected(adapterPosition, binding.option1.text.toString()) }
            binding.option2.setOnClickListener { onOptionSelected(adapterPosition, binding.option2.text.toString()) }
            binding.option3.setOnClickListener { onOptionSelected(adapterPosition, binding.option3.text.toString()) }
            binding.option4.setOnClickListener { onOptionSelected(adapterPosition, binding.option4.text.toString()) }
        }

        fun bind(question: QuestionModel) {
            // Bind the question data to the UI elements in the item layout
            binding.questionTextView.text = question.question
            binding.option1.text = question.option1
            binding.option2.text = question.option2
            binding.option3.text = question.option3
            binding.option4.text = question.option4

            // Update the radio button states based on selected answers
            updateRadioButton(binding.option1, question, adapterPosition)
            updateRadioButton(binding.option2, question, adapterPosition)
            updateRadioButton(binding.option3, question, adapterPosition)
            updateRadioButton(binding.option4, question, adapterPosition)
        }

        private fun updateRadioButton(radioButton: RadioButton, question: QuestionModel, position: Int) {
            radioButton.isChecked = selectedAnswers[position] == radioButton.text.toString()
        }
    }

    // Function to set the listener for option selection
    fun setOnOptionSelectedListener(listener: (Int, String?) -> Unit) {
        onOptionSelectedListener = listener
    }

    // Function to get the selected answer for a specific question
    fun getSelectedAnswer(position: Int): String? {
        return selectedAnswers[position]
    }

    // Function to update the selected answer for a specific question
    private fun updateSelectedAnswer(position: Int, answer: String?) {
        selectedAnswers[position] = answer
        selectedItemsCountListener.invoke(selectedItemsCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onOptionSelected(position: Int, answer: String) {
        // Check if the question is not already answered
        if (selectedAnswers[position] == null) {
            selectedItemsCount++
        }

        // Notify the listener about the option selection
        onOptionSelectedListener.invoke(position, answer)
        // Update the selected answer for the question
        updateSelectedAnswer(position, answer)
        // Notify the adapter about the data change
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = QuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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