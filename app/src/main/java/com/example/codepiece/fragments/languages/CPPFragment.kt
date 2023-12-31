package com.example.codepiece.fragments.languages

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.R
import com.example.codepiece.adapter.AdminQuestionAdapter
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

class CPPFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var adminQuestionAdapter: AdminQuestionAdapter
    private val questionList = mutableListOf<QuestionModel>() // Assuming you have a Question data class
    private var isQuestionAnswered = BooleanArray(questionList.size) { false }
    private var isLoggedIn: Boolean = false

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

        adminQuestionAdapter = AdminQuestionAdapter(questionList) { position ->
            // Handle long press
        }

        adminQuestionAdapter = AdminQuestionAdapter(questionList) { position ->
            // Handle long press
            showDeleteConfirmationDialog(requireContext(), position)
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

        binding.adminQuestionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.adminQuestionRecyclerView.adapter = adminQuestionAdapter

        binding.quizName.text = "C++ Quiz"

        // Fetch questions from Firestore
        fetchQuestionsFromFirestore()

        binding.submitButton.setOnClickListener {
            checkAllAnswers()
            binding.submitButton.visibility = View.GONE
        }
        binding.buttonUpload.setOnClickListener {
            uploadDataToFirestore()
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
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
                adminQuestionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }

    @SuppressLint("CutPasteId")
    private fun checkAllAnswers() {
        for (i in 0 until questionList.size) {
            // Accessing a specific view (answerLayout) in the questionRecyclerView
            // Uncomment this line if needed
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

    private fun showDeleteConfirmationDialog(context: Context, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Delete Question")
            .setMessage("Are you sure you want to delete this question?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteQuestionFromFirebase(position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteQuestionFromFirebase(position: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val question = questionList[position].question

        question?.let {
            firestore.collection("cpp_questions")
                .whereEqualTo("question", question) // Query to find the document.
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        document.reference.delete()
                            .addOnSuccessListener {
                                // Document deleted successfully
                                // Remove the question from the list
                                questionList.removeAt(position)
                                adminQuestionAdapter.notifyDataSetChanged()
                                Toast.makeText(
                                    requireContext(),
                                    "Question deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to delete question",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // Handle the case where the document is not found
                        Toast.makeText(
                            requireContext(),
                            "Document not found for deletion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure in querying the document
                    Toast.makeText(
                        requireContext(),
                        "Failed to query document for deletion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun uploadDataToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Assuming you have EditText components for question, options, and answer
        val question =binding.editTextQuestionUpload.text.toString()
        val option1 = binding.editTextOption1Upload.text.toString()
        val option2 = binding.editTextOption2Upload.text.toString()
        val option3 = binding.editTextOption3Upload.text.toString()
        val option4 = binding.editTextOption4Upload.text.toString()
        val answer = binding.editTextAnswerUpload.text.toString()

        // Create a data object
        val data = hashMapOf(
            "question" to question,
            "option1" to option1,
            "option2" to option2,
            "option3" to option3,
            "option4" to option4,
            "answer" to answer
        )

        // Replace "your_collection" with the actual name of your Firestore collection
        firestore.collection("cpp_questions")
            .add(data)
            .addOnSuccessListener {
                // Handle success, e.g., show a success message
                // Clear the input fields if needed
                adminQuestionAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Question uploaded successfully", Toast.LENGTH_SHORT).show()
                clearInputFields()
            }
            .addOnFailureListener { exception ->
                // Handle failure, e.g., show an error message
            }
    }

    private fun clearInputFields() {
        // Clear the input fields after successful upload
        binding.editTextQuestionUpload.text?.clear()
        binding.editTextOption1Upload.text?.clear()
        binding.editTextOption2Upload.text?.clear()
        binding.editTextOption3Upload.text?.clear()
        binding.editTextOption4Upload.text?.clear()
        binding.editTextAnswerUpload.text?.clear()
    }
}
