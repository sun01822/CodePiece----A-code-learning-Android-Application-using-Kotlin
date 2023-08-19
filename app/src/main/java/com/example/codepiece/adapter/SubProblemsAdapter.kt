package com.example.codepiece.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.data.SubProblem


class SubProblemsAdapter(
    private val subProblems: List<SubProblem>,
    private val listener: OnItemClickListener,
    private val longPressListener: OnItemLongPressListener
) :
    RecyclerView.Adapter<SubProblemsAdapter.SubProblemViewHolder>() {

    inner class SubProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val problemNumberTextView: TextView = itemView.findViewById(R.id.problemNumberTextView)
        val problemNameTextView: TextView = itemView.findViewById(R.id.problemNameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val subProblem = subProblems[position]
                    listener.onItemClick(subProblem)
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val subProblem = subProblems[position]
                    longPressListener.onOnItemLongPress(subProblem)
                }
                true // Consume the long-press event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubProblemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_problems_item, parent, false)
        return SubProblemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubProblemViewHolder, position: Int) {
        val subProblem = subProblems[position]

        // Bind data to views
        holder.problemNumberTextView.text = subProblem.problemNumber
        holder.problemNameTextView.text = subProblem.problemName
    }

    override fun getItemCount(): Int {
        return subProblems.size
    }

    interface OnItemClickListener {
        fun onItemClick(subProblem: SubProblem)
    }

    interface OnItemLongPressListener {
        fun onOnItemLongPress(subProblem: SubProblem)
    }
}
