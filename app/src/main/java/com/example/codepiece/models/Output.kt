package com.example.codepiece.models

data class FormattedResponse(
    val cpuTime: String,
    val memory: String,
    val output: String,
    val language: LanguageInfo
)

data class LanguageInfo(
    val id: String,
    val version: Int,
    val version_name: String
)