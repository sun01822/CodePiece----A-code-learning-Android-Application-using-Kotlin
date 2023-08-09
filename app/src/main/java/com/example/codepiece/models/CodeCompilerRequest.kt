package com.example.codepiece.models


data class CodeCompilerRequest(
    val language: String,
    val version: String,
    val code: String,
    val input: String?
)
