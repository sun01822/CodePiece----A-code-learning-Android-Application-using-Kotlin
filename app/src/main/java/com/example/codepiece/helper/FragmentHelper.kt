package com.example.codepiece.helper

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.codepiece.R
import com.example.codepiece.data.QuestionModel
import com.google.firebase.firestore.FirebaseFirestore

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

}
