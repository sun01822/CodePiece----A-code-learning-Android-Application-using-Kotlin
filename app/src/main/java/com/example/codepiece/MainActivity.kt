package com.example.codepiece

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

    private val BASE_URL = "https://online-code-compiler.p.rapidapi.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        codeEditText = findViewById(R.id.codeEditText)
        submitButton = findViewById(R.id.submitButton)
        codePieceTextView = findViewById(R.id.codePieceTextView)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        submitButton.setOnClickListener {
            val rawCode = codeEditText.text.toString()
            val originalCode = rawCode.trimIndent()
            val request = CodeCompilerRequest("c", "latest", originalCode, null)

            val call = apiService.compileCode(request)
            call.enqueue(object : Callback<CodeCompilerResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<CodeCompilerResponse>, response: Response<CodeCompilerResponse>) {
                    if (response.isSuccessful) {
                        val compilerResponse = response.body()
                        compilerResponse?.let {
                            codePieceTextView.text = "Output: ${it.output}\n + CPU Time: ${it.cpuTime}\n + Memory: ${it.memory}\n + Language: ${it.language}\n"
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
