package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.ProblemsAdapter
import com.example.codepiece.data.ProblemItem

class ProblemsFragment : Fragment() {
    private lateinit var problemsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_problems, container, false)
        problemsRecyclerView = rootView.findViewById(R.id.problemsRecyclerView)

        // Create a list of ProblemItems (replace with your actual data)
        val problemItems = listOf(
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/ssH8dte.jpg", "https://i.imgur.com/FtyhwyX.png", "https://i.imgur.com/5F0bfa0.png", "https://i.imgur.com/6GDJOai.png"),
                title = "If Else Related Problems",
                counterText = "10 Problems"
            ),
            // Add more ProblemItems
        )

        // Create and set the ProblemsAdapter
        val adapter = ProblemsAdapter(requireContext(), problemItems)
        problemsRecyclerView.adapter = adapter

        // Set the RecyclerView's layout manager and span count
        problemsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        return rootView
    }
}