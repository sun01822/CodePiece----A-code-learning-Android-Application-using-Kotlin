package com.example.codepiece.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.data.TeamMember

class TeamMembersAdapter(private val teamMembers: List<TeamMember>) :
    RecyclerView.Adapter<TeamMembersAdapter.TeamMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_member, parent, false)
        return TeamMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
        val teamMember = teamMembers[position]
        holder.bind(teamMember)
    }

    override fun getItemCount(): Int {
        return teamMembers.size
    }

    inner class TeamMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(teamMember: TeamMember) {
            itemView.imageView.setImageResource(teamMember.imageResId)
            itemView.textName.text = teamMember.name
            itemView.textDetails.text = teamMember.details
        }
    }
}
