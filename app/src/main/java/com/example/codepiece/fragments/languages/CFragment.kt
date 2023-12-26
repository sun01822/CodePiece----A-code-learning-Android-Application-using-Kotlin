package com.example.codepiece.fragments.languages

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.R
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

class CFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<QuestionModel>()
    private var isQuestionAnswered = BooleanArray(questionList.size) { false }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        questionAdapter = QuestionAdapter(questionList) { position, answer ->
            // Handle the selected answer, if needed
        }

        questionAdapter.setOnOptionSelectedListener { position, _ ->
            // Empty listener, you can handle selected options here if needed
            // Mark the question as answered
            isQuestionAnswered[position] = true
            // Check if all questions are answered
            val allQuestionsAnswered = isQuestionAnswered.all { it }

            // Show submit button when answered count is the same as the total number of questions
            binding.submitButton.visibility = if (allQuestionsAnswered && questionAdapter.getAnsweredCount() == questionList.size) View.VISIBLE else View.GONE

        }

        binding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionRecyclerView.adapter = questionAdapter

        binding.quizName.text = "C Programming Quiz"

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore()

        binding.submitButton.setOnClickListener {
            checkAllAnswers()
        }

        return binding.root
    }

    private fun fetchQuestionsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("c_programming_questions")

        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val question = document.toObject(QuestionModel::class.java)
                    question?.let {
                        questionList.add(it)
                    }
                }
                // Initialize isQuestionAnswered after fetching questions
                isQuestionAnswered = BooleanArray(questionList.size) { false }
                questionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }

    @SuppressLint("CutPasteId")
    private fun checkAllAnswers() {
        for (i in 0 until questionList.size) {
            val selectedAnswer = questionAdapter.getSelectedAnswer(i)
            val correctAnswer = questionList[i].answer

            val option1 = binding.questionRecyclerView.getChildAt(i)
                .findViewById<RadioButton>(R.id.option1).text.toString()
            val option2 = binding.questionRecyclerView.getChildAt(i)
                .findViewById<RadioButton>(R.id.option2).text.toString()
            val option3 = binding.questionRecyclerView.getChildAt(i)
                .findViewById<RadioButton>(R.id.option3).text.toString()
            val option4 = binding.questionRecyclerView.getChildAt(i)
                .findViewById<RadioButton>(R.id.option4).text.toString()

            if (selectedAnswer == correctAnswer) {
                val radioButton = when (selectedAnswer) {
                    option1 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1)

                    option2 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2)

                    option3 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3)

                    option4 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4)

                    else -> null
                }
                radioButton?.apply {
                    setTextColor(Color.GREEN)
                    setTypeface(null, Typeface.BOLD)
                }
            } else {
                val radioButton = when (correctAnswer) {
                    option1 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1)

                    option2 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2)

                    option3 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3)

                    option4 -> binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4)

                    else -> null
                }
                radioButton?.apply {
                    setTextColor(Color.RED)
                    setTypeface(null, Typeface.BOLD)
                }
            }
        }
        // Notify the adapter about the data change after the loop
        questionAdapter.notifyDataSetChanged()
    }
}



//            // Compare the selected answer with the correct answer
//            if (selectedAnswer == correctAnswer) {
//                // Change radio button text color to green for correct answers
//                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView).setTextColor(
//                    Color.GREEN)
//                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView2).text = correctAnswer
//            } else {
//                // Change radio button text color to red for incorrect answers
//                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView).setTextColor(
//                    Color.RED)
//                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView2).text = correctAnswer
//            }
