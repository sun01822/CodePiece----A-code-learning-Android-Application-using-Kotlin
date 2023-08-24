package com.example.codepiece.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codepiece.data.CourseData
import com.example.codepiece.databinding.CourseItemBinding

class CourseAdapter(private val courses: List<CourseData>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    inner class CourseViewHolder(private val binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseData) {
            binding.courseName.text = course.name

            // Load and display image using Glide
            Glide.with(binding.root.context)
                .load(course.imageUrl)
                .into(binding.courseImage)

            // Set click listener to open playlist URL
            binding.cardView.setOnClickListener {
                val playlistUri = Uri.parse(course.playlistUrl)
                val browserIntent = Intent(Intent.ACTION_VIEW, playlistUri)
                binding.root.context.startActivity(browserIntent)
            }
        }
    }
}
