package com.example.codepiece.helper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.AdminQuestionAdapter
import com.example.codepiece.adapter.QuestionAdapter
import com.example.codepiece.data.QuestionModel
import com.example.codepiece.databinding.FragmentQuizBinding
import com.google.firebase.firestore.FirebaseFirestore

object FragmentHelper {
    // Function to get a fragment with a value
    fun getFragmentWithValue(fragment: Fragment, value: String): Fragment {
        // Here you can pass the value to the fragment using a Bundle
        val bundle = Bundle()
        bundle.putString("VALUE_KEY", value)
        fragment.arguments = bundle

        return fragment
    }
    // You can add more helper functions as needed
    fun replaceFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    // Function to fetch questions from Firebase
    @SuppressLint("NotifyDataSetChanged")
    fun fetchQuestions(
        collectionName: String,
        questionList: MutableList<QuestionModel>,
        questionAdapter: RecyclerView.Adapter<*>,
        adminQuestionAdapter: RecyclerView.Adapter<*>,
        progressBar: ProgressBar
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection(collectionName)

        questionList.clear()

        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                progressBar.visibility = View.GONE
                for (document in querySnapshot.documents) {
                    val question = document.toObject(QuestionModel::class.java)
                    question?.let {
                        questionList.add(it)
                    }
                }
                // Initialize isQuestionAnswered after fetching questions
                val isQuestionAnswered = BooleanArray(questionList.size) { false }
                questionAdapter.notifyDataSetChanged()
                adminQuestionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }

    // Function to upload a question to Firebase
    fun uploadQuestion(
        collectionName: String,
        isUpdatingQuestion: Boolean,
        updatingPosition: Int,
        questionList: MutableList<QuestionModel>,
        questionAdapter: RecyclerView.Adapter<*>,
        adminQuestionAdapter: RecyclerView.Adapter<*>,
        binding: FragmentQuizBinding,
        context: Context
    ) {
        val firestore = FirebaseFirestore.getInstance()

        val question = binding.editTextQuestionUpload.text.toString()
        val option1 = binding.editTextOption1Upload.text.toString()
        val option2 = binding.editTextOption2Upload.text.toString()
        val option3 = binding.editTextOption3Upload.text.toString()
        val option4 = binding.editTextOption4Upload.text.toString()
        val answer = binding.editTextAnswerUpload.text.toString()

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "question" to question,
            "option1" to option1,
            "option2" to option2,
            "option3" to option3,
            "option4" to option4,
            "answer" to answer
        )

        if (isUpdatingQuestion) {
            val existingQuestion = questionList[updatingPosition]

            firestore.collection(collectionName)
                .whereEqualTo("question", existingQuestion.question)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        document.reference.update(data as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Question updated successfully", Toast.LENGTH_SHORT).show()
                                clearInputFields(binding)
                                binding.buttonUpload.text = "Upload"
                                fetchQuestions(collectionName, questionList, questionAdapter, adminQuestionAdapter, binding.progressBar)
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        } else {
            firestore.collection(collectionName)
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(context, "Question uploaded successfully", Toast.LENGTH_SHORT).show()
                    clearInputFields(binding)
                    fetchQuestions(collectionName, questionList, questionAdapter, adminQuestionAdapter, binding.progressBar)
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        }
    }


    // Function to clear the input fields
    fun clearInputFields(binding: FragmentQuizBinding) {
        binding.editTextQuestionUpload.text?.clear()
        binding.editTextOption1Upload.text?.clear()
        binding.editTextOption2Upload.text?.clear()
        binding.editTextOption3Upload.text?.clear()
        binding.editTextOption4Upload.text?.clear()
        binding.editTextAnswerUpload.text?.clear()
    }

    // Function to delete a question from Firebase
    fun deleteQuestionFromFirebase(
        collectionName: String,
        context: Context,
        questionList: MutableList<QuestionModel>,
        adminQuestionAdapter: AdminQuestionAdapter,
        position: Int
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val question = questionList[position].question

        question?.let {
            firestore.collection(collectionName)
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
                                    context,
                                    "Question deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure
                                Toast.makeText(
                                    context,
                                    "Failed to delete question",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // Handle the case where the document is not found
                        Toast.makeText(
                            context,
                            "Document not found for deletion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure in querying the document
                    Toast.makeText(
                        context,
                        "Failed to query document for deletion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    // Function to check all the answers
    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    fun checkAllAnswers(
        binding: FragmentQuizBinding,
        questionList: List<QuestionModel>,
        questionAdapter: QuestionAdapter
    ) {
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

    // Function to show the delete confirmation dialog
    fun showDeleteConfirmationDialog(
        collectionName: String,
        context: Context,
        questionList: MutableList<QuestionModel>,
        adminQuestionAdapter: AdminQuestionAdapter,
        position: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle("Delete Question")
            .setMessage("Are you sure you want to delete this question?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteQuestionFromFirebase(collectionName,context, questionList, adminQuestionAdapter, position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Function to update a question
    fun updateQuestion(
        collectionName: String,
        position: Int,
        questionList: MutableList<QuestionModel>,
        questionAdapter: RecyclerView.Adapter<*>,
        adminQuestionAdapter: RecyclerView.Adapter<*>,
        binding: FragmentQuizBinding,
        isUpdatingQuestion: Boolean,
        updatingPosition: Int,
        context: Context
    ) {
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

        binding.buttonUpload.setOnClickListener {
            uploadQuestion(
                collectionName,
                isUpdatingQuestion,
                updatingPosition,
                questionList,
                questionAdapter,
                adminQuestionAdapter,
                binding,
                context
            )
        }
    }



    // Function to show the edit confirmation dialog
    fun showEditConfirmationDialog(
        collectionName: String,
        position: Int,
        context: Context,
        questionList: MutableList<QuestionModel>,
        questionAdapter: QuestionAdapter,
        adminQuestionAdapter: AdminQuestionAdapter,
        binding: FragmentQuizBinding,
        isUpdatingQuestion: Boolean,
        updatingPosition: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle("Edit Question")
            .setMessage("Are you sure you want to edit this question?")
            .setPositiveButton("Edit") { dialog, _ ->
                updateQuestion(
                    collectionName,
                    position,
                    questionList,
                    questionAdapter,
                    adminQuestionAdapter,
                    binding,
                    isUpdatingQuestion,
                    updatingPosition,
                    context
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
