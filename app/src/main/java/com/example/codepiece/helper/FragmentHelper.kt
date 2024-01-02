package com.example.codepiece.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    fun getFragmentWithValue(fragment: Fragment, value: String): Fragment {
        val bundle = Bundle()
        bundle.putString("VALUE_KEY", value)
        fragment.arguments = bundle
        return fragment
    }

    fun replaceFragment(
        fragmentManager: FragmentManager,
        containerId: Int,
        fragment: Fragment,
        addToBackStack: Boolean = true
    ) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchQuestionsAdmin(
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
                questionAdapter.notifyDataSetChanged()
                adminQuestionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchQuestions(
        collectionName: String,
        questionList: MutableList<QuestionModel>,
        questionAdapter: RecyclerView.Adapter<*>,
        adminQuestionAdapter: RecyclerView.Adapter<*>,
        progressBar: ProgressBar
    ):  BooleanArray {
        var isQuestionAnswered = BooleanArray(questionList.size) { false }
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
                isQuestionAnswered = BooleanArray(questionList.size) { false }
                questionAdapter.notifyDataSetChanged()
                adminQuestionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the failure
            }
        return isQuestionAnswered
    }

    // Added a new parameter for the boolean array
    fun uploadQuestionToFirestore(
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
            showToast(context, "Please fill all the fields")
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
                                showToast(context, "Question updated successfully")
                                clearInputFields(binding)
                                binding.buttonUpload.text = "Upload"
                                fetchQuestionsAdmin(
                                    collectionName,
                                    questionList,
                                    questionAdapter,
                                    adminQuestionAdapter,
                                    binding.progressBar
                                )
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
                    showToast(context, "Question uploaded successfully")
                    clearInputFields(binding)
                    fetchQuestionsAdmin(
                        collectionName,
                        questionList,
                        questionAdapter,
                        adminQuestionAdapter,
                        binding.progressBar
                    )
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    showToast(context, "Failed to upload question")
                }
        }
    }

    fun clearInputFields(binding: FragmentQuizBinding) {
        binding.editTextQuestionUpload.text?.clear()
        binding.editTextOption1Upload.text?.clear()
        binding.editTextOption2Upload.text?.clear()
        binding.editTextOption3Upload.text?.clear()
        binding.editTextOption4Upload.text?.clear()
        binding.editTextAnswerUpload.text?.clear()
    }

    fun updateQuestion(
        position: Int,
        questionList: MutableList<QuestionModel>,
        binding: FragmentQuizBinding
    ) {
        val question = questionList[position]

        binding.editTextQuestionUpload.setText(question.question)
        binding.editTextOption1Upload.setText(question.option1)
        binding.editTextOption2Upload.setText(question.option2)
        binding.editTextOption3Upload.setText(question.option3)
        binding.editTextOption4Upload.setText(question.option4)
        binding.editTextAnswerUpload.setText(question.answer)

        binding.buttonUpload.text = "Update"
    }

    fun deleteQuestionFromFirebase(
        context: Context,
        questionList: MutableList<QuestionModel>,
        adminQuestionAdapter: AdminQuestionAdapter,
        position: Int
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val question = questionList[position].question

        question?.let {
            firestore.collection("cpp_questions")
                .whereEqualTo("question", question)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        document.reference.delete()
                            .addOnSuccessListener {
                                questionList.removeAt(position)
                                adminQuestionAdapter.notifyDataSetChanged()
                                showToast(context, "Question deleted successfully")
                            }
                            .addOnFailureListener { exception ->
                                showToast(context, "Failed to delete question")
                            }
                    } else {
                        showToast(context, "Document not found for deletion")
                    }
                }
                .addOnFailureListener { exception ->
                    showToast(context, "Failed to query document for deletion")
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    fun checkAllAnswers(
        binding: FragmentQuizBinding,
        questionList: List<QuestionModel>,
        questionAdapter: QuestionAdapter
    ) {
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
        questionAdapter.notifyDataSetChanged()
    }

    fun showDeleteConfirmationDialog(
        context: Context,
        questionList: MutableList<QuestionModel>,
        adminQuestionAdapter: AdminQuestionAdapter,
        position: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle("Delete Question")
            .setMessage("Are you sure you want to delete this question?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteQuestionFromFirebase(context, questionList, adminQuestionAdapter, position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun showEditConfirmationDialog(
        context: Context,
        position: Int,
        questionList: MutableList<QuestionModel>,
        binding: FragmentQuizBinding
    ) {
        AlertDialog.Builder(context)
            .setTitle("Edit Question")
            .setMessage("Are you sure you want to edit this question?")
            .setPositiveButton("Edit") { dialog, _ ->
                updateQuestion(position, questionList, binding)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
