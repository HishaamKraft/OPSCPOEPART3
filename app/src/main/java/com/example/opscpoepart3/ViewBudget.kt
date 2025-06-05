package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ViewBudget : AppCompatActivity() {
    //private lateinit var db: AppDatabase
    //private lateinit var budgetDao: BudgetDao
    private lateinit var budgetList: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budget)

        //db = AppDatabase.getDatabase(this)
        //budgetDao = db.BudgetDao()

        budgetList = findViewById(R.id.txtBudgetList)

        val close = findViewById<Button>(R.id.btnClose)
        close?.setOnClickListener{
            val intent = Intent( this,Analytics::class.java)
            startActivity(intent)
        }
        lifecycleScope.launch {
            //updateBudgetList()
        }
    }

    /*private suspend fun updateBudgetList() {
        val budget = budgetDao.getEntireBudget()
        val list = budget.joinToString("\n") { "${it.id} Category: ${it.item} \t Minimum Goal: ${it.minGoal} \t Maximum Goal: ${it.maxGoal} " }
        runOnUiThread {
            budgetList.text = list
        }
    }*/
}