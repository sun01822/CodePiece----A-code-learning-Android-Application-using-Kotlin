package com.example.codepiece.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.ModuleAdapter
import com.example.codepiece.data.ModuleItem

class HomeFragment : Fragment() {
    private lateinit var moduleAdapter: ModuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val moduleItems = listOf(
            ModuleItem("Problems", R.drawable.problems),
            ModuleItem("Courses", R.drawable.courses),
            ModuleItem("Books", R.drawable.books),
            ModuleItem("Compile Code", R.drawable.compiler),
            ModuleItem("Join Lectures", R.drawable.lectures),
            ModuleItem("Join Contest", R.drawable.contest),
            ModuleItem("Test Yourself", R.drawable.test),
            ModuleItem("Blogs", R.drawable.blog),
            ModuleItem("DSA", R.drawable.dsa),
            // Add more items as needed
        )

        moduleAdapter = ModuleAdapter(requireContext(), moduleItems)
        recyclerView.adapter = moduleAdapter

        moduleAdapter.setOnclickListener(object : ModuleAdapter.OnClickListener {
            override fun onClick(position: Int, moduleItem: ModuleItem) {
                // Handle item click
                val fragment: Fragment = when (moduleItem.name) {
                    "Problems" -> ProblemsFragment()
                    "Courses" -> CoursesFragment()
                    "Compile Code" -> CompilerFragment()
                    else -> {
                        // Handle other cases or set to a default fragment if needed
                        HomeFragment() // Replace with a default fragment or handle other cases
                    }
                }
                navigateToFragment(fragment)
            }
        })

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null) // Add to back stack for back navigation
            .commit()
    }
}
