package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*

private const val OPE = "operation"
private const val RES = "result"
private const val RESS = "result_status"

class MainActivity : AppCompatActivity() {
    //    private lateinit var result: EditText
//    private lateinit var newNumber: EditText
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }
    private var resultStatus = false

    // variables to hold operands and type of operations
    private var operand1: Double? = null
    private var pendingOperation = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // I was able to comment all this out because kotlin allows to import item
        // just like classes and it saves the work to initialize everything
        // refer to line 9 or below to see how to import all widgets on a xml file as classes(ig)
        // import kotlinx.android.synthetic.main.activity_main.*
//        result = findViewById(R.id.result)
//        newNumber = findViewById(R.id.newNumber)
//
//        // for number buttons
//        val button0 = findViewById<Button>(R.id.button0)
//        val button1 = findViewById<Button>(R.id.button1)
//        val button2 = findViewById<Button>(R.id.button2)
//        val button3 = findViewById<Button>(R.id.button3)
//        val button4 = findViewById<Button>(R.id.button4)
//        val button5 = findViewById<Button>(R.id.button5)
//        val button6 = findViewById<Button>(R.id.button6)
//        val button7 = findViewById<Button>(R.id.button7)
//        val button8 = findViewById<Button>(R.id.button8)
//        val button9 = findViewById<Button>(R.id.button9)
//        val buttonDot = findViewById<Button>(R.id.buttonDot)
//
//        // for operation buttons
//        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
//        val buttonSub = findViewById<Button>(R.id.buttonSub)
//        val buttonMul = findViewById<Button>(R.id.buttonMul)
//        val buttonDiv = findViewById<Button>(R.id.buttonDiv)
//        val buttonEqual = findViewById<Button>(R.id.buttonEqual)
//        val buttonClear = findViewById<Button>(R.id.buttonClear)

        // creating a listener for all
        val listener = View.OnClickListener { v ->
            val button = v as Button
            newNumber.append(button.text)
        }

        // setting listener on each number input button
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)
        // the thing to note here is despite of having this listener here
        // if inputType in xml doesn't have "numberSigned|numberDecimal" it wont allow me to
        // add decimal in the editText

        // creating listener for operation buttons
        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        // setting opListener on operation buttons
        buttonAdd.setOnClickListener(opListener)
        buttonSub.setOnClickListener(opListener)
        buttonDiv.setOnClickListener(opListener)
        buttonMul.setOnClickListener(opListener)
        buttonEqual.setOnClickListener(opListener)
        buttonClear.setOnClickListener {
            result.setText("")
            newNumber.setText("")
            operand1 = null
            pendingOperation = "="
            operation.text = "="
            resultStatus = false
        }

        buttonNeg.setOnClickListener {
            val neg = "-"
            if (newNumber.text.toString().isNotEmpty()) {
                if (newNumber.text.toString()[0] != '-') {
                    newNumber.setText(neg + newNumber.text.toString())
                } else {
                    newNumber.setText(newNumber.text.toString().drop(1))
                }
            } else {
                newNumber.setText(neg)
            }

        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        resultStatus = savedInstanceState.getBoolean(RESS, false)
        if (resultStatus) {
            result.setText(savedInstanceState.getString(RES).toString())
            operand1 = result.text.toString().toDouble()
        }
        pendingOperation = savedInstanceState.getString(OPE).toString()
        operation.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (resultStatus) outState.putString(RES, result.text.toString())
        outState.putBoolean(RESS, resultStatus)
        outState.putString(OPE, operation.text.toString())
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
//            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
            operand1 = value
            resultStatus = true
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // divide by zero operation handling
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }
}