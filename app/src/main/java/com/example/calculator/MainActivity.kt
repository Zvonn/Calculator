package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val viewModel: CalculatorViewModel by viewModels()
    private lateinit var resultTextView: TextView
    private lateinit var operationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.result_text)
        operationTextView = findViewById(R.id.operation_text)

        viewModel.displayValue.observe(this, Observer { resultTextView.text = it })
        viewModel.expression.observe(this, Observer { operationTextView.text = it })

        setupDigitButtons()

        findViewById<Button>(R.id.button_clear).setOnClickListener { viewModel.clear() }
        findViewById<Button>(R.id.button_equal).setOnClickListener { viewModel.calculate() }
        findViewById<Button>(R.id.button_dot).setOnClickListener { viewModel.decimal() }
        findViewById<Button>(R.id.button_plus_minus).setOnClickListener { viewModel.negate() }
        findViewById<Button>(R.id.button_percent).setOnClickListener { viewModel.percentage() }

        findViewById<Button>(R.id.button_add).setOnClickListener { viewModel.operation(Operation.ADD) }
        findViewById<Button>(R.id.button_subtract).setOnClickListener { viewModel.operation(Operation.SUBTRACT) }
        findViewById<Button>(R.id.button_multiply).setOnClickListener { viewModel.operation(Operation.MULTIPLY) }
        findViewById<Button>(R.id.button_divide).setOnClickListener { viewModel.operation(Operation.DIVIDE) }
    }

    private fun setupDigitButtons() {
        val digitButtons = arrayOf(
            findViewById<Button>(R.id.button_0),
            findViewById<Button>(R.id.button_1),
            findViewById<Button>(R.id.button_2),
            findViewById<Button>(R.id.button_3),
            findViewById<Button>(R.id.button_4),
            findViewById<Button>(R.id.button_5),
            findViewById<Button>(R.id.button_6),
            findViewById<Button>(R.id.button_7),
            findViewById<Button>(R.id.button_8),
            findViewById<Button>(R.id.button_9)
        )

        digitButtons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.digit(index) }
        }
    }
}
