package com.example.codepiece.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codepiece.R
import com.example.codepiece.data.ProblemItem

class ProblemsAdapter(private val context: Context, private val problemItems: List<ProblemItem>) :
    RecyclerView.Adapter<ProblemsAdapter.ProblemViewHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var currentItemPosition = 0

    inner class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val problemImageView: ImageView = itemView.findViewById(R.id.problemImageView)
        val problemTitleTextView: TextView = itemView.findViewById(R.id.problemTitleTextView)
        val problemCounterTextView: TextView = itemView.findViewById(R.id.problemCounterTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.problem_item, parent, false)
        return ProblemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val currentItem = problemItems[position]

        // Set data to views
        holder.problemTitleTextView.text = currentItem.title
        holder.problemCounterTextView.text = currentItem.counterText

        // Implement image slider logic
        startImageSlider(holder.problemImageView, currentItem.imageUrls)

        holder.itemView.setOnClickListener {
            // Handle item click here
        }
    }

    override fun getItemCount(): Int {
        return problemItems.size
    }

    private fun startImageSlider(imageView: ImageView, imageUrls: List<String>) {
        handler.removeCallbacksAndMessages(null) // Remove any existing callbacks
        val runnable = object : Runnable {
            override fun run() {
                Glide.with(context).load(imageUrls[currentItemPosition]).into(imageView)
                currentItemPosition = (currentItemPosition + 1) % imageUrls.size
                handler.postDelayed(this, IMAGE_SLIDER_INTERVAL)
            }
        }
        handler.post(runnable)
    }

    companion object {
        private const val IMAGE_SLIDER_INTERVAL = 3000L // Change image every 3 seconds
    }
}
