package com.example.codepiece.fragments.languages

// CFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

class CFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionAdapter: QuestionAdapter
    private val questionList = mutableListOf<QuestionModel>() // Assuming you have a Question data class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater)

        // Initialize RecyclerView
        questionAdapter = QuestionAdapter(questionList)
        binding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionRecyclerView.adapter = questionAdapter

        binding.quizName.text = "C Programming Quiz"

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore()

        binding.submitButton.setOnClickListener {

        }

        return binding.root
    }

    private fun fetchQuestionsFromFirestore() {
        // Replace "your_collection" with the actual name of your Firestore collection
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("c_programming_questions")

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
}
