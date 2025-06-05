package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

class LandingPage : AppCompatActivity(){

    //private lateinit var db: AppDatabase
    //private lateinit var ExpensesDao: ExpensesDao
    private lateinit var expenseList: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        //db = AppDatabase.getDatabase(this)
        //ExpensesDao = db.ExpensesDao()

        expenseList = findViewById(R.id.txtExpense)

        val goToBudgetPage = findViewById<Button>(R.id.btnBudget)
        goToBudgetPage?.setOnClickListener{
            val intent = Intent( this,Budget::class.java)
            startActivity(intent)
        }

        val goToExpensePage = findViewById<Button>(R.id.btnAddExpense)
        goToExpensePage?.setOnClickListener{
            val intent = Intent( this,Expenses::class.java)
            startActivity(intent)
        }

        val goToAnalyticsPage = findViewById<Button>(R.id.btnanalytics2)
        goToAnalyticsPage.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            updateExpenseList()
        }

    }
    private suspend fun updateExpenseList() {
        //val expense = ExpensesDao.getAllExpenses()
        //val list = expense.joinToString("\n") { "${it.id} Category: ${it.categoryItem} \t Description: ${it.description} \t Amount: ${it.amount} " }
        runOnUiThread {
            //expenseList.text = list
        }
    }
}