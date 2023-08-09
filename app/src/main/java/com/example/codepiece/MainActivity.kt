package com.example.codepiece

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.codepiece.adapter.CustomSpinnerAdapter
import com.example.codepiece.api.ApiService
import com.example.codepiece.models.CodeCompilerRequest
import com.example.codepiece.models.CodeCompilerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var codeEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var codePieceTextView: TextView
    private lateinit var spinner: Spinner
    private lateinit var selectedLanguage: String

    private val BASE_URL = "https://online-code-compiler.p.rapidapi.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        codeEditText = findViewById(R.id.codeEditText)
        submitButton = findViewById(R.id.submitButton)
        codePieceTextView = findViewById(R.id.codePieceTextView)
        spinner = findViewById(R.id.languageSpinner) // Get reference to the Spinner
        val languages = arrayOf("c", "cpp", "python3", "java")

        // Initialize Spinner adapter
        val customAdapter = CustomSpinnerAdapter(this, languages)
        customAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = customAdapter

        selectedLanguage = "c" // Set the default value for selectedLanguage

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position] // Update selectedLanguage when an item is selected
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected (optional)
                selectedLanguage = "c" // Set selectedLanguage to default "C"
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        submitButton.setOnClickListener {
            val rawCode = codeEditText.text.toString()
            val originalCode = rawCode.trimIndent()
            val request = CodeCompilerRequest(selectedLanguage, "latest", originalCode, null)

            val call = apiService.compileCode(request)
            call.enqueue(object : Callback<CodeCompilerResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<CodeCompilerResponse>, response: Response<CodeCompilerResponse>) {
                    if (response.isSuccessful) {
                        val compilerResponse = response.body()
                        compilerResponse?.let {
                            codePieceTextView.text = "Output: ${it.output}\nCPU Time: ${it.cpuTime}\nMemory: ${it.memory}\n${it.language}\n"
                        }
                    } else {
                        codePieceTextView.text = "Error: ${response.message()}"
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<CodeCompilerResponse>, t: Throwable) {
                    codePieceTextView.text = "Error: ${t.message}"
                }
            })
        }
    }
}
