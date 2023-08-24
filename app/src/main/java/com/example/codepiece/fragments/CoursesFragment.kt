package com.example.codepiece.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.codepiece.R
import com.example.codepiece.adapter.CourseAdapter
import com.example.codepiece.data.CourseData
import com.example.codepiece.databinding.FragmentCoursesBinding
import com.google.firebase.firestore.FirebaseFirestore

class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var firestore: FirebaseFirestore
    private var isLoggedIn: Boolean = false
    private val courses: MutableList<CourseData> = ArrayList()
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize RecyclerView and set up the adapter
        courseAdapter = CourseAdapter(courses)
        binding.courseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh the data when the user swipes down
            fetchCoursesFromFirestore()
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is logged in using SharedPreferences
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set up FloatingActionButton click listener
        binding.floatingActionButton.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        binding.floatingActionButton.setOnClickListener {
            // Navigate to UploadCourseFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val uploadCourseFragment = UploadCourseFragment()

            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, uploadCourseFragment)
                .addToBackStack(null) // Add transaction to the back stack
                .commit()
        }

        // Fetch courses from Firestore and display them
        fetchCoursesFromFirestore()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchCoursesFromFirestore() {
        swipeRefreshLayout.isRefreshing = true // Start the refreshing animation

        firestore.collection("courses")
            .get()
            .addOnCompleteListener { task ->
                swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation

                if (task.isSuccessful) {
                    courses.clear() // Clear the previous data
                    for (document in task.result) {
                        val title = document.getString("title")
                        val imageUrl = document.getString("imageUrl")
                        val playlistUrl = document.getString("playlistUrl")
                        courses.add(CourseData(title!!, imageUrl!!, playlistUrl!!))
                    }
                    // Notify the adapter that data has changed
                    courseAdapter.notifyDataSetChanged()
                } else {
                    // Handle error
                }
            }
    }
}
