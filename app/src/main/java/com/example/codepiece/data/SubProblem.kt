package com.example.codepiece.data


data class SubProblem(
    val problemNumber: String,
    val problemName: String,
    val problemAlgorithms: String, // Add this field
    val cprogramCode: String, // Add this field
    val cppCode: String, // Add this field
    val javaCode: String, // Add this field
    val pythonCode: String // Add this field
)

