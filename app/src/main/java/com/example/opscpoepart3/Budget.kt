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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Budget : AppCompatActivity() {

    //private lateinit var db: AppDatabase
    //private lateinit var budgetDao: BudgetDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        //db = AppDatabase.getDatabase(this)
        //budgetDao = db.BudgetDao()

        val goToAnalytics = findViewById<Button>(R.id.btnAdd)
        goToAnalytics.setOnClickListener {

            val txtCategory = findViewById<EditText>(R.id.edtCategory)
            val txtMin = findViewById<EditText>(R.id.edtMinGoal)
            val txtMax = findViewById<EditText>(R.id.edtMaxGoal)

            val category = txtCategory.text.toString()
            val minGoal = txtMin.text.toString()
            val maxGoal = txtMax.text.toString()

            if(category.isEmpty()){
                txtCategory.error="Please enter category"
                return@setOnClickListener
            }

            if (minGoal > maxGoal) {
                txtMax.error = "Your maximum goal should be greater than your min goal"
                txtMax.text.clear()
            }


            /*if (minGoal.isNotBlank() && maxGoal.isNotBlank() && selectedItem != "Select option") {
                lifecycleScope.launch {
                    budgetDao.insert(
                        com.example.poepart2.Data.Budget(
                            item = selectedItem,
                            minGoal = minGoal,
                            maxGoal = maxGoal
                        )
                    )
                }
            }*/



            val intent = Intent( this,ViewBudget::class.java)
            startActivity(intent)
        }
        val returnToLanding = findViewById<Button>(R.id.btnReturn3)
        returnToLanding.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }
    }
}

