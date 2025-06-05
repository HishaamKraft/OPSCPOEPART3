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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Analytics : AppCompatActivity() {

    //private lateinit var db: AppDatabase
    //private lateinit var expensesDao: ExpensesDao

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

        //db = AppDatabase.getDatabase(this)
        //expensesDao = db.ExpensesDao()

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

        display.setOnClickListener {
            val startDateText = startDateDisplay.text.toString()
            val endDateText = endDateDisplay.text.toString()

            if (startDateText.isEmpty() || endDateText.isEmpty()) {
                Toast.makeText(this, "Please select both start and end dates.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDate = format.parse(startDateText)
            val endDate = format.parse(endDateText)
            val txtExpenseResults = findViewById<TextView>(R.id.txtExpensesDisplay)


            if (startDate != null && endDate != null) {
                when {
                    startDate == endDate -> {
                        Toast.makeText(
                            this,
                            "Start date cannot be equal to end date.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    startDate.after(endDate) -> {
                        Toast.makeText(
                            this,
                            "Start date cannot be after end date.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    endDate.before(startDate) -> {
                        Toast.makeText(
                            this,
                            "End date cannot be before start date.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener

                    }
                    else -> {
                        // Valid range - proceed with query
                        /*lifecycleScope.launch {
                            val expensesBetween = withContext(Dispatchers.IO) {
                                expensesDao.getExpensesBetweenDates(startDate, endDate)
                            }

                            if (expensesBetween.isEmpty()) {
                                txtExpenseResults.text = "No expenses found in this range."
                            } else {
                                val formattedText = expensesBetween.joinToString("\n\n") { expense ->
                                    "Date: ${expense.expenseDate}\n" +
                                            "Category: ${expense.categoryItem}\n" +
                                            "Description: ${expense.description}\n" +
                                            "Amount: R${expense.amount}"
                                }

                                txtExpenseResults.text = formattedText

                            }
                        }*/


                    }
                }
            }
        }
        val returnToLanding = findViewById<Button>(R.id.btnReturn2)
        returnToLanding.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

        val moveToViewBudget = findViewById<Button>(R.id.btnViewBudget)
        moveToViewBudget.setOnClickListener {
            val intent = Intent(this, ViewBudget::class.java)
            startActivity(intent)
        }
    }

}