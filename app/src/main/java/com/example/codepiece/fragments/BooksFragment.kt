package com.example.codepiece.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.codepiece.R
import com.example.codepiece.adapter.BooksAdapter
import com.example.codepiece.data.Book
import com.example.codepiece.databinding.FragmentBooksBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.GridLayoutManager

class BooksFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var firestore: FirebaseFirestore
    private var isLoggedIn: Boolean = false
    private val books: MutableList<Book> = ArrayList()
    private lateinit var booksAdapter: BooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize RecyclerView and set up the BooksAdapter
        booksAdapter = BooksAdapter(books)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // Set up GridLayoutManager with 2 columns
            adapter = booksAdapter
        }

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh the data when the user swipes down
            fetchBooksFromFirestore()
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is logged in using SharedPreferences
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set up FloatingActionButton click listener
        binding.floatingActionButton.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        binding.floatingActionButton.setOnClickListener {
            // Navigate to UploadBooksFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val uploadBooksFragment = UploadBooksFragment()

            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, uploadBooksFragment)
                .addToBackStack(null) // Add transaction to the back stack
                .commit()
        }

        // Fetch books from Firestore and display them
        fetchBooksFromFirestore()

        return view
    }

    private fun fetchBooksFromFirestore() {
        swipeRefreshLayout.isRefreshing = true

        firestore.collection("books")
            .get()
            .addOnCompleteListener { task ->
                swipeRefreshLayout.isRefreshing = false

                if (task.isSuccessful) {
                    books.clear()
                    for (document in task.result) {
                        val title = document.getString("title")
                        val author = document.getString("author")
                        val downloadLink = document.getString("downloadLink")
                        val imageUrl = document.getString("imageUrl")
                        books.add(Book(title!!, author!!, downloadLink!!, imageUrl!!))
                    }
                    booksAdapter.notifyDataSetChanged()
                } else {
                    // Handle error
                }
            }
    }
}
