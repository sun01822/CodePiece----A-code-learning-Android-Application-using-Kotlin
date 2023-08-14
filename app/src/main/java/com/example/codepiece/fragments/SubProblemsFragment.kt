package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codepiece.R
import com.example.codepiece.databinding.FragmentSubProblemsBinding

class SubProblemsFragment : Fragment() {
    private lateinit var binding : FragmentSubProblemsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubProblemsBinding.inflate(layoutInflater)
        // Retrieve passed data from arguments
        val problemName = arguments?.getString("problemName")
        binding.textView.text = problemName
        return binding.root
    }
}