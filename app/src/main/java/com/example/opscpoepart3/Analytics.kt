package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Analytics : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_analytics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance().getReference("Expenses")

        val startDateSelection = findViewById<Button>(R.id.btnStartDate)
        val startDateDisplay = findViewById<TextView>(R.id.txtStartDateDisplay)
        val cal = Calendar.getInstance()
        val myStartYear = cal.get(Calendar.YEAR)
        val myStartMonth = cal.get(Calendar.MONTH)
        val myStartDay = cal.get(Calendar.DAY_OF_MONTH)

        startDateSelection.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                startDateDisplay.text = formattedDate
            }, myStartYear, myStartMonth, myStartDay)
            datePickerDialog.show()
        }

        val endDateSelection = findViewById<Button>(R.id.btnEndDate)
        val endDateDisplay = findViewById<TextView>(R.id.txtEndDateDisplay)
        val myEndYear = cal.get(Calendar.YEAR)
        val myEndMonth = cal.get(Calendar.MONTH)
        val myEndDay = cal.get(Calendar.DAY_OF_MONTH)

        endDateSelection.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                endDateDisplay.text = formattedDate
            }, myEndYear, myEndMonth, myEndDay)
            datePickerDialog.show()
        }

        val display = findViewById<Button>(R.id.btnDisplayExpenses)
        val txtExpenseResults = findViewById<TextView>(R.id.txtExpensesDisplay)

        display.setOnClickListener {
            val startDateText = startDateDisplay.text.toString()
            val endDateText = endDateDisplay.text.toString()

            if (startDateText.isEmpty() || endDateText.isEmpty()) {
                Toast.makeText(this, "Please select both start and end dates.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val startDate = try {
                inputFormat.parse(startDateText)
            } catch (e: Exception) {
                null
            }
            val endDate = try {
                inputFormat.parse(endDateText)
            } catch (e: Exception) {
                null
            }

            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Date format is invalid.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when {
                startDate == endDate -> {
                    Toast.makeText(this, "Start date cannot be equal to end date.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                startDate.after(endDate) -> {
                    Toast.makeText(this, "Start date cannot be after end date.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val formattedStartDate = outputFormat.format(startDate)
            val formattedEndDate = outputFormat.format(endDate)

            // Clear previous results
            txtExpenseResults.text = "Loading expenses..."

            // Query Firebase RTDB for expenses between formattedStartDate and formattedEndDate
            val query = database.orderByChild("date").startAt(formattedStartDate).endAt(formattedEndDate)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        txtExpenseResults.text = "No expenses found in this date range."
                        return
                    }

                    val expensesList = mutableListOf<String>()

                    for (expenseSnapshot in snapshot.children) {
                        val expense = expenseSnapshot.getValue(ExpensesData::class.java)
                        if (expense != null) {
                            // Build a formatted string for each expense
                            val formattedAmount = String.format(Locale.getDefault(), "%.2f", expense.amount ?: 0.0)

                            val expenseText = "Date: ${expense.date}\n" +
                                    "Category: ${expense.category}\n" +
                                    "Description: ${expense.description}\n" +
                                    "Amount: R$formattedAmount"
                            expensesList.add(expenseText)
                        }
                    }

                    if (expensesList.isEmpty()) {
                        txtExpenseResults.text = "No expenses found in this date range."
                    } else {
                        txtExpenseResults.text = expensesList.joinToString(separator = "\n\n")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    txtExpenseResults.text = ""
                    Toast.makeText(this@Analytics, "Failed to load expenses: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val returnToLanding = findViewById<Button>(R.id.btnReturn2)
        returnToLanding.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        val moveToAdvancedAnalytics = findViewById<Button>(R.id.btnViewAdvancedAnalytics)
        moveToAdvancedAnalytics.setOnClickListener {
            val intent = Intent(this, AdvancedAnalytics::class.java)
            startActivity(intent)
        }
    }
}
