package com.example.codepiece.helper


import com.example.codepiece.api.ApiService
import com.example.codepiece.models.CodeCompilerRequest
import com.example.codepiece.models.CodeCompilerResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {

    private const val BASE_URL = "https://online-code-compiler.p.rapidapi.com/"

    private val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    fun compileCode(language: String, code: String, input: String, callback: Callback<CodeCompilerResponse>) {
        val request = if (input.isEmpty()) {
            CodeCompilerRequest(language, "latest", code, null)
        } else {
            CodeCompilerRequest(language, "latest", code, input)
        }
        val call = apiService.compileCode(request)
        call.enqueue(callback)
    }
}
