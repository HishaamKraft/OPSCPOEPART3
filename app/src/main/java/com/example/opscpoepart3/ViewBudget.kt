package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ViewBudget : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var budgetListView: ListView
    private lateinit var budgetList: ArrayList<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budget)

        budgetListView = findViewById(R.id.listViewBudgets)
        budgetList = ArrayList()

        database = FirebaseDatabase.getInstance().getReference("Budgets")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                budgetList.clear()
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetData::class.java)
                    budget?.let {
                        val display = "Category: ${it.category}\nMin: ${it.minGoal}, Max: ${it.maxGoal}"
                        budgetList.add(display)
                    }
                }

                val adapter = ArrayAdapter(this@ViewBudget, android.R.layout.simple_list_item_1, budgetList)
                budgetListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewBudget, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })

        val close = findViewById<Button>(R.id.btnClose)
        close?.setOnClickListener{
            val intent = Intent( this,LandingPage::class.java)
            startActivity(intent)
        }


    }
}