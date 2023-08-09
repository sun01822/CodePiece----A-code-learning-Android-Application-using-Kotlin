package com.example.codepiece.api

import com.example.codepiece.models.CodeCompilerRequest
import com.example.codepiece.models.CodeCompilerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers(
        "content-type: application/json",
        "X-RapidAPI-Key: 4eb9e442d4mshe75760479e178afp17bf9bjsne6494e0da592",
        "X-RapidAPI-Host: online-code-compiler.p.rapidapi.com"
    )
    @POST("v1/")
    fun compileCode(@Body request: CodeCompilerRequest): Call<CodeCompilerResponse>
}
