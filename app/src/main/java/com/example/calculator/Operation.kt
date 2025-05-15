package com.example.calculator

enum class Operation(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷")
}

fun String.toDoubleOrNull(): Double? {
    return try {
        this.toDouble()
    } catch (e: NumberFormatException) {
        null
    }
}