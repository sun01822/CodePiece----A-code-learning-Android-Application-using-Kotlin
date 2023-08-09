package com.example.codepiece.models

data class CodeCompilerResponse(
    val cpuTime: String,
    val memory: String,
    val output: String,
    val language: LanguageInfo
)
