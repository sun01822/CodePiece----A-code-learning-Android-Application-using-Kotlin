package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.TeamMembersAdapter
import com.example.codepiece.data.TeamMember
import com.example.codepiece.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding
    private val teamMembers = mutableListOf<TeamMember>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)

        // Add team member data to the list
        teamMembers.add(TeamMember(R.drawable.rahat, "Al Shafiullah", "Developer"))
        teamMembers.add(TeamMember(R.drawable.shams, "Shams Khan Sun", "Developer"))
        teamMembers.add(TeamMember(R.drawable.antora, "Anika Tabassum Antora", "Designer"))
        // Add more team members as needed

        // Initialize RecyclerView
        val recyclerView: RecyclerView = binding.recyclerViewAboutUs
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val teamMembersAdapter = TeamMembersAdapter(teamMembers)
        recyclerView.adapter = teamMembersAdapter

        return binding.root
    }
}
