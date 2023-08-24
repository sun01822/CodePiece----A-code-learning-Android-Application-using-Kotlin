package com.example.codepiece.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codepiece.adapter.CourseAdapter
import com.example.codepiece.data.CourseData
import com.example.codepiece.databinding.FragmentCoursesBinding
import com.google.firebase.firestore.FirebaseFirestore

class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore
    private var isLoggedIn: Boolean = false
    private val courses: MutableList<CourseData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize RecyclerView and set up the adapter
        val courseAdapter = CourseAdapter(courses)
        binding.courseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is logged in using SharedPreferences
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set up FloatingActionButton click listener
        binding.floatingActionButton.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        binding.floatingActionButton.setOnClickListener { v ->
            // Implement logic to show dialog or activity for adding new course
        }

        fetchCoursesFromFirestore(courseAdapter)

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCoursesFromFirestore(adapter: CourseAdapter) {
        firestore.collection("courses")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    courses.clear()
                    for (document in task.result) {
                        val name = document.getString("name")
                        val imageUrl = document.getString("imageUrl")
                        val playlistUrl = document.getString("playlistUrl")
                        courses.add(CourseData(name!!, imageUrl!!, playlistUrl!!))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle error
                }
            }
    }
}
