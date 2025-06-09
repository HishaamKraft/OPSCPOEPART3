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
    private lateinit var btnClose: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budget)

        budgetListView = findViewById(R.id.listViewBudgets)
        btnClose = findViewById(R.id.btnClose)
        budgetList = ArrayList()

        database = FirebaseDatabase.getInstance().getReference("Budgets")

        loadBudgetData()

        btnClose.setOnClickListener {
            startActivity(Intent(this, LandingPage::class.java))
        }
        val invite = findViewById<Button>(R.id.btnInvite)
        invite.setOnClickListener {
            val intent = Intent(this,InviteCollab::class.java)
            startActivity(intent)
        }
    }

    private fun loadBudgetData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                budgetList.clear()
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(BudgetData::class.java)
                    budget?.let {
                        val displayText = """
                            Category: ${it.category}
                            Min Goal: R${String.format("%.2f", it.minGoal)}
                            Max Goal: R${String.format("%.2f", it.maxGoal)}
                        """.trimIndent()
                        budgetList.add(displayText)
                    }
                }

                val adapter = ArrayAdapter(this@ViewBudget, android.R.layout.simple_list_item_1, budgetList)
                budgetListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewBudget, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


