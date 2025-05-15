package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.DecimalFormat

class CalculatorViewModel : ViewModel() {

    private val _displayValue = MutableLiveData<String>("0")
    val displayValue: LiveData<String> = _displayValue

    private val _expression = MutableLiveData<String>("")
    val expression: LiveData<String> = _expression

    private var firstOperand: Double = 0.0
    private var pendingOperation: Operation? = null
    private var shouldClearDisplay: Boolean = false

    private val decimalFormat = DecimalFormat("#.##########")

    fun digit(digit: Int) {
        if (shouldClearDisplay) {
            _displayValue.value = digit.toString()
            shouldClearDisplay = false
        } else {
            val currentValue = _displayValue.value ?: "0"
            _displayValue.value = if (currentValue == "0") digit.toString() else currentValue + digit
        }
    }

    fun decimal() {
        if (shouldClearDisplay) {
            _displayValue.value = "0."
            shouldClearDisplay = false
        } else if (!(_displayValue.value ?: "").contains(".")) {
            _displayValue.value = (_displayValue.value ?: "0") + "."
        }
    }

    fun clear() {
        _displayValue.value = "0"
        _expression.value = ""
        firstOperand = 0.0
        pendingOperation = null
        shouldClearDisplay = false
    }

    fun negate() {
        val currentValue = _displayValue.value ?: "0"
        _displayValue.value = if (currentValue.startsWith("-")) {
            currentValue.substring(1)
        } else if (currentValue != "0") {
            "-$currentValue"
        } else {
            currentValue
        }
    }

    fun percentage() {
        val currentValue = (_displayValue.value ?: "0").toDoubleOrNull() ?: 0.0
        val result = currentValue / 100.0
        _displayValue.value = formatForDisplay(result)
    }

    fun operation(operation: Operation) {
        calculate()
        val currentValue = (_displayValue.value ?: "0").toDoubleOrNull() ?: 0.0
        firstOperand = currentValue
        pendingOperation = operation
        _expression.value = "${formatForDisplay(firstOperand)} ${operation.symbol}"
        shouldClearDisplay = true
    }

    fun calculate() {
        val currentValue = (_displayValue.value ?: "0").toDoubleOrNull() ?: 0.0

        if (pendingOperation != null) {
            val result = when (pendingOperation) {
                Operation.ADD -> firstOperand + currentValue
                Operation.SUBTRACT -> firstOperand - currentValue
                Operation.MULTIPLY -> firstOperand * currentValue
                Operation.DIVIDE -> {
                    if (currentValue == 0.0) {
                        _displayValue.value = "Ошибка"
                        _expression.value = ""
                        shouldClearDisplay = true
                        return
                    }
                    firstOperand / currentValue
                }
                else -> currentValue
            }

            _expression.value = "${formatForDisplay(firstOperand)} ${pendingOperation?.symbol} ${formatForDisplay(currentValue)} ="
            _displayValue.value = formatForDisplay(result)
            pendingOperation = null
            firstOperand = result
            shouldClearDisplay = true
        }
    }

    private fun formatForDisplay(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            decimalFormat.format(value)
        }
    }
}
