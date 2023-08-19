package com.example.codepiece.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.SubProblemsAdapter
import com.example.codepiece.data.SubProblem
import com.example.codepiece.databinding.FragmentSubProblemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class SubProblemsFragment : Fragment(), SubProblemsAdapter.OnItemClickListener, SubProblemsAdapter.OnItemLongPressListener {
    private lateinit var binding: FragmentSubProblemsBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var problemType: String
    private lateinit var sharedPreferences: SharedPreferences // Declare sharedPreferences variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubProblemsBinding.inflate(layoutInflater)
        problemType = arguments?.getString("problemName").toString()
        binding.textView.text = problemType

        // Initialize RecyclerView and set up the layout manager
        val recyclerView: RecyclerView = binding.subProblemsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch subProblems data from Firebase Firestore
        fetchSubProblemsData()

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is logged in using SharedPreferences
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set visibility of addButton based on the user's login status
        binding.addButton.visibility = if (isLoggedIn) View.VISIBLE else View.GONE

        binding.addButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("problemType", problemType)
                // Only add problemType to the bundle
            }

            val uploadFragment = UploadProblemFragment()
            uploadFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, uploadFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun fetchSubProblemsData() {
        firestore.collection(problemType)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val subProblems = mutableListOf<SubProblem>()
                for (document in querySnapshot.documents) {
                    val problemNumber = document.getString("problemNumber") ?: ""
                    val problemName = document.getString("problemName") ?: ""
                    val problemAlgorithms = document.getString("problemAlgorithms") ?: ""
                    val cprogramCode = document.getString("cprogramCode") ?: ""
                    val cppCode = document.getString("cppCode") ?: ""
                    val javaCode = document.getString("javaCode") ?: ""
                    val pythonCode = document.getString("pythonCode") ?: ""
                    subProblems.add(
                        SubProblem(
                            problemNumber,
                            problemName,
                            problemAlgorithms,
                            cprogramCode,
                            cppCode,
                            javaCode,
                            pythonCode
                        )
                    )
                }

                // Sort the subProblems list based on problemNumber
                subProblems.sortBy { it.problemNumber.toInt() }

                // Update the adapter with the sorted data
                val recyclerView: RecyclerView = binding.subProblemsRecyclerView
                val subProblemsAdapter = SubProblemsAdapter(subProblems, this, this)
                recyclerView.adapter = subProblemsAdapter
            }
            .addOnFailureListener {
                // Handle error here
                Toast.makeText(requireContext(), "Check your data connection", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onItemClick(subProblem: SubProblem) {
        val bundle = Bundle().apply {
            putString("problemType", problemType)
            putString("problemNumber", subProblem.problemNumber)
            putString("problemName", subProblem.problemName)
            putString("problemAlgorithms", subProblem.problemAlgorithms)
            putString("cprogramCode", subProblem.cprogramCode)
            putString("cppCode", subProblem.cppCode)
            putString("javaCode", subProblem.javaCode)
            putString("pythonCode", subProblem.pythonCode)
        }

        val detailsFragment = DetailsScreenFragment()
        detailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onOnItemLongPress(subProblem: SubProblem) {
        // Show an alert dialog for confirmation before deleting
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Problem")
            .setMessage("Are you sure you want to delete this problem?")
            .setPositiveButton("Delete") { _, _ ->
                deleteSubProblem(subProblem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun deleteSubProblem(subProblem: SubProblem) {
        val problemNumber = subProblem.problemNumber
        firestore.collection(problemType)
            .whereEqualTo("problemNumber", problemNumber) // Query to find the document
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    document.reference.delete()
                        .addOnSuccessListener {
                            // Problem deleted successfully
                            parentFragmentManager.popBackStack()
                            Toast.makeText(requireContext(), "Problem deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            // Handle error
                            Toast.makeText(requireContext(), "Error deleting problem", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                // Handle error
                Toast.makeText(requireContext(), "Check your data connection", Toast.LENGTH_SHORT).show()
            }
    }
}
