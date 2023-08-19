package com.example.codepiece.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.codepiece.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var problemType: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        problemType = arguments?.getString("problemType").toString()

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Check if the user is already logged in
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Hide the login views and show the logout button
            binding.loginLayout.visibility = View.GONE
            binding.logout.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            val email = binding.email.editableText.toString()
            val password = binding.password.editableText.toString()

            // Here, you can perform your login logic using the email and password variables.
            // For example, you can make an API call to authenticate the user.
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Empty password", Toast.LENGTH_SHORT).show()
            } else {
                if (email == "admin" && password == "654321") {
                    // Save the logged-in state using SharedPreferences
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                    // Hide the login views and show the logout button
                    binding.loginLayout.visibility = View.GONE
                    binding.logout.visibility = View.VISIBLE

                    Toast.makeText(requireContext(), "Log in successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.logout.setOnClickListener {
            // Clear the logged-in state using SharedPreferences
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

            // Show the login views and hide the logout button
            binding.loginLayout.visibility = View.VISIBLE
            binding.logout.visibility = View.GONE

            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
        }
    }
}
