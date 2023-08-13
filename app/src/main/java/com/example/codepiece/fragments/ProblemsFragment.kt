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
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/ZKdWijl.png", "https://i.imgur.com/oWqu0Bs.png", "https://i.imgur.com/Zp4FDi3.jpg", "https://i.imgur.com/DQT5HhQ.png"),
                title = "Array-String Related Problems",
                counterText = "10 Problems"
            ),
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/wEh67Lp.png", "https://i.imgur.com/OHwaNa6.jpg", "https://i.imgur.com/xpvIEfN.jpg", "https://i.imgur.com/zsPsk2A.jpg"),
                title = "Function Related Problems",
                counterText = "10 Problems"
            ),
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/DM5FtKU.jpg", "https://i.imgur.com/uFwEk2L.jpg", "https://i.imgur.com/bmOzvPU.png", "https://i.imgur.com/SlCiZNa.png"),
                title = "Loop Related Problems",
                counterText = "10 Problems"
            ),
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/vQbnCpO.png", "https://i.imgur.com/VtlgYes.png", "https://i.imgur.com/zpYZbjP.png", "https://i.imgur.com/AlUa7W2.gif"),
                title = "Pointer Related Problems",
                counterText = "10 Problems"
            ),
            ProblemItem(
                imageUrls = listOf("https://i.imgur.com/rnCxHHK.jpg", "https://i.imgur.com/3iUTxWU.jpg", "https://i.imgur.com/q3aZvtY.jpg", "https://i.imgur.com/bR6INdX.jpg"),
                title = "Pattern Related Problems",
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