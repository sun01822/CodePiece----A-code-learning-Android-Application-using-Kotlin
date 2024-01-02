package com.example.codepiece.fragments.languages

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.adapter.AdminQuestionAdapter
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.example.codepiece.helper.FragmentHelper.checkAllAnswers
import com.example.codepiece.helper.FragmentHelper.fetchQuestions
import com.example.codepiece.helper.FragmentHelper.showDeleteConfirmationDialog
import com.example.codepiece.helper.FragmentHelper.showEditConfirmationDialog
import com.example.codepiece.helper.FragmentHelper.uploadQuestion

class CPPFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var adminQuestionAdapter: AdminQuestionAdapter
    private val questionList = mutableListOf<QuestionModel>() // Assuming you have a Question data class
    private var isQuestionAnswered = BooleanArray(questionList.size) { false }
    private var isLoggedIn: Boolean = false
    private var isUpdatingQuestion = false
    private var updatingPosition = -1
    private var collectionName = "cpp_questions"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater)
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is logged in using SharedPreferences
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        // Set up FloatingActionButton click listener
        binding.quizLayout.visibility = if (isLoggedIn) View.GONE else View.VISIBLE
        binding.adminLayout.visibility = if (isLoggedIn) View.VISIBLE else View.GONE

        questionAdapter = QuestionAdapter(questionList) { position, answer ->
            // Handle the selected answer, if needed
        }

        adminQuestionAdapter = AdminQuestionAdapter(questionList,
            onLongPressListener = { position ->
                showDeleteConfirmationDialog(
                    collectionName,
                    requireContext(),
                    questionList,
                    adminQuestionAdapter,
                    position
                )},
            onPressListener = { position ->
                showEditConfirmationDialog(
                    collectionName,
                    position,
                    requireContext(),
                    questionList,
                    questionAdapter,
                    adminQuestionAdapter,
                    binding,
                    true,
                    position
                )
            }
        )


        binding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionRecyclerView.adapter = questionAdapter

        binding.adminQuestionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.adminQuestionRecyclerView.adapter = adminQuestionAdapter

        binding.quizName.text = "C++ Quiz"
        binding.progressBar.visibility = View.VISIBLE

        // Fetch questions from Firestore
        fetchQuestions(
            collectionName,
            questionList,
            questionAdapter,
            adminQuestionAdapter,
            binding.progressBar
        )

        questionAdapter.setOnOptionSelectedListener { position, _ ->
            // Empty listener, you can handle selected options here if needed
            // Mark the question as answered
            //isQuestionAnswered[position] = true
            // Check if all questions are answered
            val allQuestionsAnswered = isQuestionAnswered.all { it }

            // Show submit button when answered count is the same as the total number of questions
            binding.submitButton.visibility =
                if (allQuestionsAnswered && questionAdapter.getAnsweredCount() == questionList.size) View.VISIBLE
                else View.GONE
        }

        binding.submitButton.setOnClickListener {
            checkAllAnswers(binding, questionList, questionAdapter)
            binding.submitButton.visibility = View.GONE
        }
        binding.buttonUpload.setOnClickListener {
            uploadQuestion(
                collectionName,
                isUpdatingQuestion,
                updatingPosition,
                questionList,
                questionAdapter,
                adminQuestionAdapter,
                binding,
                requireContext()
            )
        }
        return binding.root
    }
}