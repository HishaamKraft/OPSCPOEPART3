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

class Budget : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        database = FirebaseDatabase.getInstance().getReference("Budgets")

        val goToAnalytics = findViewById<Button>(R.id.btnAdd)
        goToAnalytics.setOnClickListener {

            val txtCategory = findViewById<EditText>(R.id.edtCategory)
            val txtMin = findViewById<EditText>(R.id.edtMinGoal)
            val txtMax = findViewById<EditText>(R.id.edtMaxGoal)

            val category = txtCategory.text.toString()
            val minGoal = txtMin.text.toString()
            val maxGoal = txtMax.text.toString()

            if (category.isNotEmpty() && maxGoal.isNotEmpty() && minGoal.isNotEmpty()) {
                val budgetId = database.push().key
                val budgetData = BudgetData(category, minGoal, maxGoal)

                if (budgetId != null) {
                    database.child(budgetId).setValue(budgetData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Budget added", Toast.LENGTH_SHORT).show()
                            txtCategory.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to add budget", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            } else {
                Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show()

                /*if(category.isEmpty()){
                txtCategory.error="Please enter category"
                return@setOnClickListener
            }

            if (minGoal > maxGoal) {
                txtMax.error = "Your maximum goal should be greater than your min goal"
                txtMax.text.clear()
            }*/
                val intent = Intent(this, ViewBudget::class.java)
                startActivity(intent)
            }
            val returnToLanding = findViewById<Button>(R.id.btnReturn3)
            returnToLanding.setOnClickListener {
                val intent = Intent(this, LandingPage::class.java)
                startActivity(intent)
            }
        }
    }
}

