package com.example.codepiece.fragments

import android.annotation.SuppressLint
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
    private lateinit var problemType: String
    private lateinit var problemNumber: String
    private lateinit var problemName: String
    private lateinit var problemAlgorithms: String
    private lateinit var cprogramCode: String
    private lateinit var cppCode: String
    private lateinit var javaCode: String
    private lateinit var pythonCode: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadProblemBinding.inflate(inflater, container, false)
        problemType = arguments?.getString("problemType").toString()
        val update = arguments?.getBoolean("update") ?: false

        if (update) {
            problemType = arguments?.getString("problemType").toString()
            problemNumber = arguments?.getString("problemNumber").toString()
            problemName = arguments?.getString("problemName").toString()
            problemAlgorithms = arguments?.getString("problemAlgorithms").toString()
            cprogramCode = arguments?.getString("cprogramCode").toString()
            cppCode = arguments?.getString("cppCode").toString()
            javaCode = arguments?.getString("javaCode").toString()
            pythonCode = arguments?.getString("pythonCode").toString()

            binding.editTextProblemNumber.setText(problemNumber)
            binding.editTextProblemName.setText(problemName)
            binding.editTextAlgorithm.setText(problemAlgorithms)
            binding.editTextCProgramCode.setText(cprogramCode)
            binding.editTextCppCode.setText(cppCode)
            binding.editTextJavaCode.setText(javaCode)
            binding.editTextPythonCode.setText(pythonCode)

            binding.buttonUpload.text = "Update"
            binding.buttonUpload.setOnClickListener {
                updateProblemData()
            }
        } else {
            binding.buttonUpload.setOnClickListener {
                uploadProblemData()
            }
        }
        return binding.root
    }

    private fun uploadProblemData() {
        // Retrieve data from EditText fields
        problemNumber = binding.editTextProblemNumber.text.toString()
        problemName = binding.editTextProblemName.text.toString()
        problemAlgorithms = binding.editTextAlgorithm.text.toString()
        cprogramCode = binding.editTextCProgramCode.text.toString()
        cppCode = binding.editTextCppCode.text.toString()
        javaCode = binding.editTextJavaCode.text.toString()
        pythonCode = binding.editTextPythonCode.text.toString()

        // Create a map to store problem data
        val problemData = hashMapOf(
            "problemNumber" to problemNumber,
            "problemName" to problemName,
            "problemAlgorithms" to problemAlgorithms,
            "cprogramCode" to cprogramCode,
            "cppCode" to cppCode,
            "javaCode" to javaCode,
            "pythonCode" to pythonCode
        )

        // Add data to Firestore
        firestore.collection(problemType)
            .add(problemData)
            .addOnSuccessListener {
                // Problem uploaded successfully
                parentFragmentManager.popBackStack()
                Toast.makeText(requireContext(), "Uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Handle error
                Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProblemData() {
        // Retrieve data from EditText fields
        problemNumber = binding.editTextProblemNumber.text.toString()
        problemName = binding.editTextProblemName.text.toString()
        problemAlgorithms = binding.editTextAlgorithm.text.toString()
        cprogramCode = binding.editTextCProgramCode.text.toString()
        cppCode = binding.editTextCppCode.text.toString()
        javaCode = binding.editTextJavaCode.text.toString()
        pythonCode = binding.editTextPythonCode.text.toString()

        // Create a map to store updated problem data
        val updatedProblemData = hashMapOf(
            "problemNumber" to problemNumber,
            "problemName" to problemName,
            "problemAlgorithms" to problemAlgorithms,
            "cprogramCode" to cprogramCode,
            "cppCode" to cppCode,
            "javaCode" to javaCode,
            "pythonCode" to pythonCode
        )

        // Update data in Firestore using the unique document ID
        firestore.collection(problemType)
            .whereEqualTo("problemNumber", problemNumber) // Query to find the document
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    document.reference.update(updatedProblemData as Map<String, Any>)
                        .addOnSuccessListener {
                            // Problem updated successfully
                            parentFragmentManager.popBackStack()
                            parentFragmentManager.popBackStack()
                            Toast.makeText(requireContext(), "Updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            // Handle error
                            Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                // Handle error
                Toast.makeText(requireContext(), "Check your internet connection", Toast.LENGTH_SHORT).show()
            }
    }
}
