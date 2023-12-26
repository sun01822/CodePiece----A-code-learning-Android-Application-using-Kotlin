package com.example.codepiece.fragments.languages

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.R
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

class CPPFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<QuestionModel>() // Assuming you have a Question data class
    private var isQuestionAnswered = BooleanArray(questionList.size) { false }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater)

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


        binding.quizName.text = "C++ Quiz"

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore()

        binding.submitButton.setOnClickListener {
            checkAllAnswers()
            binding.submitButton.visibility = View.GONE
        }

        return binding.root
    }
    private fun fetchQuestionsFromFirestore() {
        // Replace "your_collection" with the actual name of your Firestore collection
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("cpp_questions")

        // Fetch 10 random questions
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
//            binding.questionRecyclerView.getChildAt(i)
//                .findViewById<LinearLayout>(R.id.answerLayout).visibility = View.VISIBLE
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
                if (selectedAnswer == option1) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1).setTextColor(Color.GREEN)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (selectedAnswer == option2) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2).setTextColor(Color.GREEN)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (selectedAnswer == option3) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3).setTextColor(Color.GREEN)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (selectedAnswer == option4) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4).setTextColor(Color.GREEN)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4)
                    radioButton.setTypeface(null, Typeface.BOLD)
                }
            } else {
                if (correctAnswer == option1) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1).setTextColor(Color.RED)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option1)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (correctAnswer == option2) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2).setTextColor(Color.RED)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option2)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (correctAnswer == option3) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3).setTextColor(Color.RED)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option3)
                    radioButton.setTypeface(null, Typeface.BOLD)
                } else if (correctAnswer == option4) {
                    binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4).setTextColor(Color.RED)
                    val radioButton = binding.questionRecyclerView.getChildAt(i)
                        .findViewById<RadioButton>(R.id.option4)
                    radioButton.setTypeface(null, Typeface.BOLD)
                }
            }
        }
        // Notify the adapter about the data change after the loop
        questionAdapter.notifyDataSetChanged()
    }
}