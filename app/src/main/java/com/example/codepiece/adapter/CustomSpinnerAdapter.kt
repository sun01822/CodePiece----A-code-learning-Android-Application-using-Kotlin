package com.example.codepiece.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.codepiece.R

class CustomSpinnerAdapter(context: Context, private val languages: Array<String>) :
    ArrayAdapter<String>(context, R.layout.custom_spinner_item, languages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    @SuppressLint("ServiceCast")
    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_spinner_item, parent, false)

        val languageImage = view.findViewById<ImageView>(R.id.languageImage)
        val languageName = view.findViewById<TextView>(R.id.languageName)

        // Customize the circular image based on the position or language
        // For example, you can set different images for each language
        when (getItem(position)) {
            "c" -> languageImage.setImageResource(R.drawable.c)
            "cpp" -> languageImage.setImageResource(R.drawable.cpp)
            "python3" -> languageImage.setImageResource(R.drawable.java)
            "java" -> languageImage.setImageResource(R.drawable.python)
            // Add more cases for other languages
        }

        languageName.text = getItem(position)

        return view
    }
}
