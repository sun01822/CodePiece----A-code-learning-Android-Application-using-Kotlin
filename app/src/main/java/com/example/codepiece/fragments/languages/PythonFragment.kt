package com.example.codepiece.fragments.languages

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.R
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

class PythonFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<QuestionModel>() // Assuming you have a Question data class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater)

        questionAdapter = QuestionAdapter(questionList) { position, answer ->
            // Handle the selected answer, if needed
        }

        questionAdapter.setOnOptionSelectedListener { position, answer ->
            // Handle the selected answer, if needed
        }

        binding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionRecyclerView.adapter = questionAdapter

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
        val collectionRef = firestore.collection("python_questions")

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
                questionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }
    private fun checkAllAnswers() {
        for (i in 0 until questionList.size) {
            binding.questionRecyclerView.getChildAt(i).findViewById<LinearLayout>(R.id.answerLayout).visibility = View.VISIBLE
            val selectedAnswer = questionAdapter.getSelectedAnswer(i)
            val correctAnswer = questionList[i].answer

            // Compare the selected answer with the correct answer
            if (selectedAnswer == correctAnswer) {
                // Change radio button text color to green for correct answers
                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView).setTextColor(
                    Color.GREEN)
                //  binding.questionRecyclerView.getChildAt(i).findViewById<RadioButton>(R.id.option1).setTextColor(
                //                    Color.GREEN)
                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView2).text = correctAnswer
            } else {
                // Change radio button text color to red for incorrect answers
                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView).setTextColor(
                    Color.RED)
                binding.questionRecyclerView.getChildAt(i).findViewById<TextView>(R.id.answerTextView2).text = correctAnswer
            }
        }
        // Notify the adapter about the data change after the loop
        questionAdapter.notifyDataSetChanged()
    }
}