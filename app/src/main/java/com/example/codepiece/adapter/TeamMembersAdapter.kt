package com.example.codepiece.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.data.TeamMember
import com.example.codepiece.databinding.ItemTeamMemberBinding

class TeamMembersAdapter(private val teamMembers: List<TeamMember>) :
    RecyclerView.Adapter<TeamMembersAdapter.TeamMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val binding = ItemTeamMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        val teamMember = teamMembers[position]
        holder.bind(teamMember)
    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class TeamMemberViewHolder(private val binding: ItemTeamMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamMember: TeamMember) {
            binding.imageView.setImageResource(teamMember.imageResId)
            binding.textName.text = teamMember.name
            binding.textDetails.text = teamMember.details
        }
    }
}
