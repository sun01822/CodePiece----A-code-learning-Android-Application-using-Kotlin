package com.example.codepiece.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.codepiece.databinding.FragmentUploadProblemBinding
import com.google.firebase.firestore.FirebaseFirestore

class UploadProblemFragment : Fragment() {

    private lateinit var binding: FragmentUploadProblemBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var problemType : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadProblemBinding.inflate(inflater, container, false)
        problemType = arguments?.getString("problemType").toString()
        setupUploadButton()
        return binding.root
    }

    private fun setupUploadButton() {
        binding.buttonUpload.setOnClickListener {
            val problemNumber = binding.editTextProblemNumber.text.toString()
            val problemName = binding.editTextProblemName.text.toString()
            val cprogramCode = binding.editTextCProgramCode.text.toString()
            val cppCode = binding.editTextCppCode.text.toString()
            val javaCode = binding.editTextJavaCode.text.toString()
            val pythonCode = binding.editTextPythonCode.text.toString()

            val problemData = hashMapOf(
                "problemNumber" to problemNumber,
                "problemName" to problemName,
                "cprogramCode" to cprogramCode,
                "cppCode" to cppCode,
                "javaCode" to javaCode,
                "pythonCode" to pythonCode
            )

            firestore.collection(problemType) // Replace with your collection name
                .add(problemData)
                .addOnSuccessListener {
                    // Problem uploaded successfully
                    // You can show a success message or navigate back
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.popBackStack()
                    Toast.makeText(requireContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Handle error here
                    // You can show an error message or handle the error in any way you want
                    Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
