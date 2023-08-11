package com.example.codepiece.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.codepiece.R
import com.example.codepiece.adapter.CustomSpinnerAdapter
import com.example.codepiece.databinding.FragmentCompilerBinding
import com.example.codepiece.helper.ApiHelper
import com.example.codepiece.models.CodeCompilerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompilerFragment : Fragment() {
    private lateinit var binding: FragmentCompilerBinding
    private lateinit var selectedLanguage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompilerBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val languages = arrayOf("c", "cpp", "python3", "java")

        // Initialize Spinner adapter
        val customAdapter = CustomSpinnerAdapter(requireContext(), languages)
        customAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
        binding.languageSpinner.adapter = customAdapter

        selectedLanguage = "c"

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedLanguage = "c"
            }
        }

        binding.submitButton.setOnClickListener {
            val rawCode = binding.codeEditText.text.toString()
            val input = binding.inputEditText.text.toString()
            val originalCode = rawCode.trimIndent()

            ApiHelper.compileCode(selectedLanguage, originalCode, input, object : Callback<CodeCompilerResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<CodeCompilerResponse>, response: Response<CodeCompilerResponse>) {
                    if (response.isSuccessful) {
                        val compilerResponse = response.body()
                        compilerResponse?.let {
                            binding.codePieceTextView.text = "Output: ${it.output}\nCPU Time: ${it.cpuTime}\nMemory: ${it.memory}\n${it.language}\n"
                        }
                    } else {
                        binding.codePieceTextView.text = "Error: ${response.message()}"
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<CodeCompilerResponse>, t: Throwable) {
                    binding.codePieceTextView.text = "Error: ${t.message}"
                }
            })
        }

        return rootView
    }
}
