package com.example.codepiece.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.codepiece.databinding.FragmentQuizBinding

object FragmentHelper {
    fun getFragmentWithValue(fragment: Fragment, value: String): Fragment {
        // Here you can pass the value to the fragment using a Bundle
        val bundle = Bundle()
        bundle.putString("VALUE_KEY", value)
        fragment.arguments = bundle

        return fragment
    }
    // You can add more helper functions as needed
    fun replaceFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    // Function to clear the input fields
    fun clearInputFields(binding: FragmentQuizBinding) {
        binding.editTextQuestionUpload.text?.clear()
        binding.editTextOption1Upload.text?.clear()
        binding.editTextOption2Upload.text?.clear()
        binding.editTextOption3Upload.text?.clear()
        binding.editTextOption4Upload.text?.clear()
        binding.editTextAnswerUpload.text?.clear()
    }

}
