package com.example.codepiece.fragments.languages

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.R
import com.example.codepiece.adapter.AdminQuestionAdapter
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.example.codepiece.helper.FragmentHelper.clearInputFields
import com.google.firebase.firestore.FirebaseFirestore

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
            onLongPressListener = { position -> showDeleteConfirmationDialog(requireContext(), position) },
            onPressListener = { position -> showEditConfirmationDialog(requireContext(), position) })

        questionAdapter.setOnOptionSelectedListener { position, _ ->
            // Empty listener, you can handle selected options here if needed
            // Mark the question as answered
            isQuestionAnswered[position] = true
            // Check if all questions are answered
            val allQuestionsAnswered = isQuestionAnswered.all { it }

            // Show submit button when answered count is the same as the total number of questions
            binding.submitButton.visibility =
                if (allQuestionsAnswered && questionAdapter.getAnsweredCount() == questionList.size) View.VISIBLE
                else View.GONE
        }

        binding.questionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.questionRecyclerView.adapter = questionAdapter

        binding.adminQuestionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.adminQuestionRecyclerView.adapter = adminQuestionAdapter

        binding.quizName.text = "C++ Quiz"
        binding.progressBar.visibility = View.VISIBLE

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

        // Clear the existing questions before fetching new ones
        questionList.clear()

        // Fetch 10 random questions
        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                binding.progressBar.visibility = View.GONE
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
                Toast.makeText(requireContext(), "Failed to fetch questions", Toast.LENGTH_SHORT).show()
                // Handle the failure
            }
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
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

    private fun showEditConfirmationDialog(context: Context, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Edit Question")
            .setMessage("Are you sure you want to edit this question?")
            .setPositiveButton("Edit") { dialog, _ ->
                updateQuestion(position)
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

    private fun updateQuestion(position: Int) {
        // Set the flag to indicate that we are updating a question
        isUpdatingQuestion = true
        updatingPosition = position

        // Fetch the question data
        val question = questionList[position]

        // Set the data to the EditText fields
        binding.editTextQuestionUpload.setText(question.question)
        binding.editTextOption1Upload.setText(question.option1)
        binding.editTextOption2Upload.setText(question.option2)
        binding.editTextOption3Upload.setText(question.option3)
        binding.editTextOption4Upload.setText(question.option4)
        binding.editTextAnswerUpload.setText(question.answer)

        // Change the text of the upload button to "Update"
        binding.buttonUpload.text = "Update"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun uploadDataToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Assuming you have EditText components for question, options, and answer
        val question = binding.editTextQuestionUpload.text.toString()
        val option1 = binding.editTextOption1Upload.text.toString()
        val option2 = binding.editTextOption2Upload.text.toString()
        val option3 = binding.editTextOption3Upload.text.toString()
        val option4 = binding.editTextOption4Upload.text.toString()
        val answer = binding.editTextAnswerUpload.text.toString()

        if(question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

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
        if (isUpdatingQuestion) {
            // If updating, use the updatingPosition to get the document ID of the existing question
            val existingQuestion = questionList[updatingPosition]

            firestore.collection("cpp_questions")
                .whereEqualTo("question", existingQuestion.question)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        document.reference.update(data as Map<String, Any>)
                            .addOnSuccessListener {
                                // Handle success, e.g., show a success message
                                Toast.makeText(requireContext(), "Question updated successfully", Toast.LENGTH_SHORT).show()

                                // Clear the input fields after successful update
                                clearInputFields(binding)

                                // Reset the upload button text and flags
                                binding.buttonUpload.text = "Upload"
                                isUpdatingQuestion = false
                                updatingPosition = -1
                                // Fetch the updated data from Firestore
                                fetchQuestionsFromFirestore()
                                adminQuestionAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure, e.g., show an error message
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure in querying the document
                    Toast.makeText(
                        requireContext(),
                        "Failed to query document for updating",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            // If not updating, add a new question
            firestore.collection("cpp_questions")
                .add(data)
                .addOnSuccessListener {
                    // Handle success, e.g., show a success message
                    Toast.makeText(requireContext(), "Question uploaded successfully", Toast.LENGTH_SHORT).show()

                    // Clear the input fields after successful upload
                    clearInputFields(binding)
                    // Fetch the updated data from Firestore
                    fetchQuestionsFromFirestore()
                    adminQuestionAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Handle failure, e.g., show an error message
                }
        }
    }
}