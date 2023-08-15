package com.example.codepiece.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.codepiece.R
import com.example.codepiece.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var problemType : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        problemType = arguments?.getString("problemType").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            val email = binding.email.editableText.toString()
            val password = binding.password.editableText.toString()

            // Here, you can perform your login logic using the email and password variables.
            // For example, you can make an API call to authenticate the user.
           if(email.isEmpty()){
               Toast.makeText(requireContext(), "Enter valid email", Toast.LENGTH_SHORT).show()
           }
            else if(password.isEmpty()){
                Toast.makeText(requireContext(), "Empty password", Toast.LENGTH_SHORT).show()
            }
            else{
               if (email == "admin" && password == "654321") {
                   val bundle = Bundle().apply {
                       putString("problemType", problemType) // Only add problemType to the bundle
                   }

                   val uploadFragment = UploadProblemFragment()
                   uploadFragment.arguments = bundle

                   parentFragmentManager.beginTransaction()
                       .replace(R.id.fragmentContainer, uploadFragment)
                       .addToBackStack(null)
                       .commit()

                   // Finish the current fragment
                   parentFragmentManager.beginTransaction()
                       .remove(this)
                       .commit()

                   Toast.makeText(requireContext(), "Log in successful", Toast.LENGTH_SHORT).show()
               }
               else{
                   Toast.makeText(requireContext(), "Wrong email or password", Toast.LENGTH_SHORT).show()
                }
           }
        }
    }

}