package com.example.codepiece.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codepiece.R
import com.example.codepiece.databinding.FragmentDetailsScreenBinding

class DetailsScreenFragment : Fragment() {
    private lateinit var binding: FragmentDetailsScreenBinding
    private lateinit var problemType: String
    private lateinit var selectedButton : String
    private lateinit var problemNumber : String
    private lateinit var problemName : String
    private lateinit var problemAlgorithms : String
    private lateinit var cprogramCode : String
    private lateinit var cppCode : String
    private lateinit var javaCode : String
    private lateinit var pythonCode : String
    private lateinit var sharedPreferences: SharedPreferences // Declare sharedPreferences variable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentDetailsScreenBinding.inflate(inflater, container, false)
        problemType = arguments?.getString("problemType").toString()
        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        // Check if the user is logged in using SharedPreferences
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn){
            binding.editProblemButton.visibility = View.VISIBLE
        } else {
            binding.editProblemButton.visibility = View.GONE
        }

        // Inside onViewCreated after setting click listeners for the buttons
        binding.editProblemButton.setOnClickListener {
            // Create a bundle with the data to pass to UploadProblemsFragment
            val bundle = Bundle().apply {
                putString("problemType", problemType)
                putString("problemNumber", problemNumber)
                putString("problemName", problemName)
                putString("problemAlgorithms", problemAlgorithms)
                putString("cprogramCode", cprogramCode)
                putString("cppCode", cppCode)
                putString("javaCode", javaCode)
                putString("pythonCode", pythonCode)
                putBoolean("update", true) // Set the update key
            }

            val uploadFragment = UploadProblemFragment()
            uploadFragment.arguments = bundle

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, uploadFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.copyCodeButton.setOnClickListener {
            // Get the code from the TextView
            val code = binding.codeTextView.text.toString()

            // Copy the code to the clipboard
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Code", code)
            clipboard.setPrimaryClip(clip)

            // Show a toast indicating code copied
            Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        binding.changeThemeButton.setBackgroundResource(R.drawable.ic_theme)
        // Set click listener for the changeThemeButton
        binding.changeThemeButton.setOnClickListener {
            // Change the theme of the codeTextView
            val currentTextColor = binding.codeTextView.currentTextColor
            if (currentTextColor == ContextCompat.getColor(requireContext(), R.color.black)) {
                binding.codeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.codeCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.copyCodeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                binding.changeThemeButton.setBackgroundResource(R.drawable.ic_dark)
            } else {
                binding.codeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.codeCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.copyCodeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                binding.changeThemeButton.setBackgroundResource(R.drawable.ic_theme)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from the bundle
        problemNumber = arguments?.getString("problemNumber").toString()
        problemName = arguments?.getString("problemName").toString()
        problemAlgorithms = arguments?.getString("problemAlgorithms").toString()
        cprogramCode = arguments?.getString("cprogramCode").toString()
        cppCode = arguments?.getString("cppCode").toString()
        javaCode = arguments?.getString("javaCode").toString()
        pythonCode = arguments?.getString("pythonCode").toString()

        // Set data to the layout's views using View Binding
        binding.problemNumberTextView.text = problemNumber
        binding.problemNameTextView.text = problemName
        binding.problemAlgorithmsTextView.text = problemAlgorithms

        // Set the default selected state for the cButton and codeTextView
        selectedButton = "c"
        setButtonStyle(binding.cButton)
        resetButtonStyles(binding.cppButton, binding.javaButton, binding.pythonButton)
        binding.codeTextView.text = cprogramCode

        // Set click listeners for buttons
        binding.cppButton.setOnClickListener {
            selectedButton = "cpp"
            // Update the text of codeTextView to show C++ code
            binding.codeTextView.text = cppCode

            // Change the button style of cppButton
            setButtonStyle(binding.cppButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.javaButton, binding.pythonButton
            )
        }

        binding.cButton.setOnClickListener {
            selectedButton = "c"
            // Update the text of codeTextView to show C code
            binding.codeTextView.text = cprogramCode

            // Change the button style of cButton
            setButtonStyle(binding.cButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cppButton, binding.javaButton, binding.pythonButton
            )
        }

        binding.javaButton.setOnClickListener {
            selectedButton = "java"
            // Update the text of codeTextView to show Java code
            binding.codeTextView.text = javaCode

            // Change the button style of javaButton
            setButtonStyle(binding.javaButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.cppButton, binding.pythonButton
            )
        }

        binding.pythonButton.setOnClickListener {
            selectedButton = "python"
            // Update the text of codeTextView to show Python code
            binding.codeTextView.text = pythonCode

            // Change the button style of pythonButton
            setButtonStyle(binding.pythonButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.cppButton, binding.javaButton
            )
        }

        binding.runButton.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer2, CompilerFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit() // You need to add this line
        }
    }

    private fun setButtonStyle(button: Button) {
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorAccent))
    }

    private fun resetButtonStyles(vararg buttons: Button) {
        for (button in buttons) {
            button.setTextColor(ContextCompat.getColor(button.context, android.R.color.black))
        }
    }
}
