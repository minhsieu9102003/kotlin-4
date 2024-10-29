package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sourceAmountEditText: EditText
    private lateinit var targetAmountEditText: EditText
    private lateinit var sourceCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner

    private var exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "JPY" to 153.25,
        "GBP" to 0.77,
        "AUD" to 1.52
        // Add more currencies if needed
    )

    private val currencyDecimalPlaces = mapOf(
        "USD" to 2,
        "EUR" to 2,
        "JPY" to 0,
        "GBP" to 2,
        "AUD" to 2
    )

    private var isSourceAmountFocused = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to our new layout
        setContentView(R.layout.activity_main)

        // Initialize views
        sourceAmountEditText = findViewById(R.id.sourceAmountEditText)
        targetAmountEditText = findViewById(R.id.targetAmountEditText)
        sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)

        // Set touch listeners to change focus
        sourceAmountEditText.setOnTouchListener { _, _ ->
            isSourceAmountFocused = true
            sourceAmountEditText.isEnabled = true
            targetAmountEditText.isEnabled = false
            false
        }

        targetAmountEditText.setOnTouchListener { _, _ ->
            isSourceAmountFocused = false
            targetAmountEditText.isEnabled = true
            sourceAmountEditText.isEnabled = false
            false
        }

        // Add TextWatchers
        sourceAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isSourceAmountFocused) {
                    performConversion()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        targetAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isSourceAmountFocused) {
                    performConversion()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Spinner listeners
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        sourceCurrencySpinner.onItemSelectedListener = spinnerListener
        targetCurrencySpinner.onItemSelectedListener = spinnerListener
    }

    private fun performConversion() {
        try {
            // Determine source and target currencies
            val sourceCurrency = if (isSourceAmountFocused) {
                sourceCurrencySpinner.selectedItem.toString()
            } else {
                targetCurrencySpinner.selectedItem.toString()
            }

            val targetCurrency = if (isSourceAmountFocused) {
                targetCurrencySpinner.selectedItem.toString()
            } else {
                sourceCurrencySpinner.selectedItem.toString()
            }

            // Get amount from the focused EditText
            val amountText = if (isSourceAmountFocused) {
                sourceAmountEditText.text.toString()
            } else {
                targetAmountEditText.text.toString()
            }

            val amount = amountText.toDoubleOrNull() ?: 0.0

            // Retrieve exchange rates
            val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
            val targetRate = exchangeRates[targetCurrency] ?: 1.0

            // Perform the conversion
            val convertedAmount = if (sourceRate != 0.0) {
                amount * (targetRate / sourceRate)
            } else {
                0.0
            }

            // Get decimal places for the target currency
            val decimalPlaces = currencyDecimalPlaces[targetCurrency] ?: 2
            val formattedAmount = "%.${decimalPlaces}f".format(convertedAmount)

            // Update the non-focused EditText
            if (isSourceAmountFocused) {
                targetAmountEditText.setText(formattedAmount)
            } else {
                sourceAmountEditText.setText(formattedAmount)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
