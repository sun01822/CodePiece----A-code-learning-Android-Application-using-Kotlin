package com.example.codepiece.fragments.languages

import android.annotation.SuppressLint
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
import com.example.codepiece.helper.FragmentHelper.buildDialog
import com.example.codepiece.helper.FragmentHelper.checkAllAnswers
import com.example.codepiece.helper.FragmentHelper.checkAnswer
import com.example.codepiece.helper.FragmentHelper.delayFunctionExecution
import com.example.codepiece.helper.FragmentHelper.fetchQuestions
import com.example.codepiece.helper.FragmentHelper.showDeleteConfirmationDialog
import com.example.codepiece.helper.FragmentHelper.showEditConfirmationDialog
import com.example.codepiece.helper.FragmentHelper.uploadQuestion
import java.util.concurrent.CountDownLatch

class CFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var adminQuestionAdapter: AdminQuestionAdapter
    private val questionList = mutableListOf<QuestionModel>()
    private var isLoggedIn: Boolean = false
    private var isUpdatingQuestion = false
    private var updatingPosition = -1
    private var collectionName = "c_programming_questions"

    @SuppressLint("NotifyDataSetChanged")
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

        questionAdapter = QuestionAdapter(
            questionList,
            onOptionSelectedListener = { position, answer ->
                // Handle the selected answer, if needed
            },
            selectedItemsCountListener = { selectedItemsCount ->
                // Check if all questions are answered
                val allQuestionsAnswered = selectedItemsCount == questionList.size
                // Show submit button when all questions are answered
                binding.submitButton.visibility =
                    if (allQuestionsAnswered) View.VISIBLE else View.GONE
            }
        )

        adminQuestionAdapter = AdminQuestionAdapter(
            questionList,
            onLongPressListener = { position ->
                showDeleteConfirmationDialog(
                    collectionName,
                    requireContext(),
                    questionList,
                    adminQuestionAdapter,
                    position
                )
            },
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

        binding.quizName.text = "C Programming Quiz"
        binding.progressBar.visibility = View.VISIBLE

        // Fetch questions from Firestore
        val showList = fetchQuestions(
            collectionName,
            questionList,
            questionAdapter,
            adminQuestionAdapter,
            binding.progressBar,
            isLoggedIn
        )

        binding.submitButton.setOnClickListener {
            checkAnswer(
                showList,
                questionAdapter,
                binding.questionRecyclerView
            )
            delayFunctionExecution(1500) {
                val (correctCount, wrongCount) = checkAllAnswers(
                    binding,
                    showList,
                    questionAdapter
                )
                val quizResultDialog = buildDialog(
                    requireContext(),
                    correctCount,
                    wrongCount
                )
                quizResultDialog.show()
            }
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