package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codepiece.R
import com.example.codepiece.databinding.FragmentJoinContestBinding

class JoinContestFragment : Fragment() {
    private lateinit var binding : FragmentJoinContestBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentJoinContestBinding.inflate(layoutInflater)
        return binding.root










    }
}