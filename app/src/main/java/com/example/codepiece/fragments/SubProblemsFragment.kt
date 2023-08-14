package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.adapter.SubProblemsAdapter
import com.example.codepiece.data.SubProblem
import com.example.codepiece.databinding.FragmentSubProblemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class SubProblemsFragment : Fragment() {
    private lateinit var binding: FragmentSubProblemsBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var problemName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubProblemsBinding.inflate(layoutInflater)
        problemName = arguments?.getString("problemName").toString()
        binding.textView.text = problemName

        // Initialize RecyclerView and set up the layout manager
        val recyclerView: RecyclerView = binding.subProblemsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*uploadProblemsToFirestore()*/
        // Fetch subProblems data from Firebase Firestore
        fetchSubProblemsData()
        return binding.root
    }

    /* private fun uploadProblemsToFirestore() {
         val problems = listOf(
             SubProblem("1", "Write a program to find maximum between two numbers."),
             SubProblem("2", "Write a program to find maximum between three numbers"),
             // Add more problems as needed
         )

         for (problem in problems) {
             firestore.collection(problemName) // Replace with your collection name
                 .add(problem)
                 .addOnSuccessListener { documentReference ->
                     // Problem uploaded successfully
                     Toast.makeText(requireContext(), "Problem uploaded", Toast.LENGTH_SHORT).show()
                     println("Problem uploaded: ${documentReference.id}")
                 }
                 .addOnFailureListener { exception ->
                     // Handle error here
                     Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                     println("Error uploading problem: $exception")
                 }
         }
     }*/

    private fun fetchSubProblemsData() {
        firestore.collection(problemName) // Replace with your collection name
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val subProblems = mutableListOf<SubProblem>()
                for (document in querySnapshot.documents) {
                    val problemNumber = document.getString("problemNumber") ?: ""
                    val problemName = document.getString("problemName") ?: ""
                    subProblems.add(SubProblem(problemNumber, problemName))
                }

                // Sort the subProblems list based on problemNumber
                subProblems.sortBy { it.problemNumber.toInt() }

                // Update the adapter with the sorted data
                val recyclerView: RecyclerView = binding.subProblemsRecyclerView
                val subProblemsAdapter = SubProblemsAdapter(subProblems)
                recyclerView.adapter = subProblemsAdapter
            }
            .addOnFailureListener { exception ->
                // Handle error here
                Toast.makeText(requireContext(), "Check your data connection", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
