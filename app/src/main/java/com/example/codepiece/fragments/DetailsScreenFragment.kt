package com.example.codepiece.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
    private lateinit var selectedButton : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentDetailsScreenBinding.inflate(inflater, container, false)

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

        // Set click listener for the changeThemeButton
        binding.changeThemeButton.setOnClickListener {
            // Change the theme of the codeTextView
            val currentTextColor = binding.codeTextView.currentTextColor
            if (currentTextColor == ContextCompat.getColor(requireContext(), R.color.black)) {
                binding.codeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.codeCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.copyCodeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                binding.changeThemeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.codeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.codeCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.copyCodeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                binding.changeThemeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from the bundle
        val problemNumber = arguments?.getString("problemNumber")
        val problemName = arguments?.getString("problemName")
        val problemAlgorithms = arguments?.getString("problemAlgorithms")
        val cprogramCode = arguments?.getString("cprogramCode")
        val cppCode = arguments?.getString("cppCode")
        val javaCode = arguments?.getString("javaCode")
        val pythonCode = arguments?.getString("pythonCode")

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
