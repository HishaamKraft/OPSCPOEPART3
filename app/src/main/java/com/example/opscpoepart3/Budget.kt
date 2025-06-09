package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlin.math.max

class Budget : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var spinnerCategory: Spinner
    private lateinit var txtMin: EditText
    private lateinit var txtMax: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnReturn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        database = FirebaseDatabase.getInstance().getReference("Budgets")

        spinnerCategory = findViewById(R.id.edtCategory)
        txtMin = findViewById(R.id.edtMinGoal)
        txtMax = findViewById(R.id.edtMaxGoal)
        btnAdd = findViewById(R.id.btnAdd)
        btnReturn = findViewById(R.id.btnReturn3)

        val categories = arrayOf(
            "Select category",
            "Rent/Mortgage",
            "Transport",
            "Shopping",
            "Entertainment",
            "Groceries",
            "Toiletries",
            "Utilities",
            "Insurance"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        btnAdd.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem.toString()
            val minGoalText = txtMin.text.toString()
            val maxGoalText = txtMax.text.toString()

            if (selectedCategory == "Select category" || minGoalText.isEmpty() || maxGoalText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields and select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val minGoal: Double
            val maxGoal: Double

            try {
                minGoal = minGoalText.toDouble()
                maxGoal = maxGoalText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter valid numbers for goals", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budgetId = database.push().key
            val budgetData = BudgetData(selectedCategory, minGoal, maxGoal)

            if (budgetId != null) {
                database.child(budgetId).setValue(budgetData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Budget added", Toast.LENGTH_SHORT).show()

                        // Clear inputs
                        spinnerCategory.setSelection(0)
                        txtMin.text.clear()
                        txtMax.text.clear()

                        // Navigate
                        startActivity(Intent(this, ViewBudget::class.java))
                        finish()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "Failed to add budget: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        btnReturn.setOnClickListener {
            startActivity(Intent(this, LandingPage::class.java))
        }
    }
}

