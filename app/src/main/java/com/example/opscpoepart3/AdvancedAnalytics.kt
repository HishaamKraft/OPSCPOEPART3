package com.example.opscpoepart3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AdvancedAnalytics : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var dbExpenses: DatabaseReference
    private lateinit var dbBudgets: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_analytics)

        barChart = findViewById(R.id.barChart)
        dbExpenses = FirebaseDatabase.getInstance().getReference("Expenses")
        dbBudgets = FirebaseDatabase.getInstance().getReference("Budgets")

        loadChartData()

        val returnToRegularAnalytics = findViewById<Button>(R.id.btnReturn5)
        returnToRegularAnalytics.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
        }
    }

    private fun loadChartData() {
        val expenseTotalsByCategory = mutableMapOf<String, Double>()

        dbExpenses.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(expenseSnapshot: DataSnapshot) {
                for (expenseNode in expenseSnapshot.children) {
                    val expense = expenseNode.getValue(ExpensesData::class.java)
                    val category = expense?.category ?: continue
                    val amount = expense.amount ?: 0.0
                    expenseTotalsByCategory[category] =
                        expenseTotalsByCategory.getOrDefault(category, 0.0) + amount
                }

                dbBudgets.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(budgetSnapshot: DataSnapshot) {
                        val usedEntries = ArrayList<BarEntry>()
                        val maxEntries = ArrayList<BarEntry>()
                        val labels = ArrayList<String>()
                        var index = 0

                        for (budgetNode in budgetSnapshot.children) {
                            val budget = budgetNode.getValue(BudgetData::class.java)
                            val category = budget?.category ?: continue
                            val amountUsed = expenseTotalsByCategory.getOrDefault(category, 0.0)
                            val maxBudget = budget.maxGoal ?: 0.0

                            usedEntries.add(BarEntry(index.toFloat(), amountUsed.toFloat()))
                            maxEntries.add(BarEntry(index.toFloat(), maxBudget.toFloat()))
                            labels.add(category)
                            index++
                        }

                        val usedDataSet = BarDataSet(usedEntries, "Amount Used").apply {
                            color = resources.getColor(R.color.purple_500, null)
                            valueTextSize = 10f
                            setDrawValues(true)
                        }

                        val maxDataSet = BarDataSet(maxEntries, "Max Budget").apply {
                            color = resources.getColor(R.color.teal_700, null)
                            valueTextSize = 10f
                            setDrawValues(true)
                        }

                        val barData = BarData(usedDataSet, maxDataSet)
                        val barWidth = 0.3f
                        val barSpace = 0.05f
                        val groupSpace = 0.3f

                        barData.barWidth = barWidth
                        barChart.data = barData

                        val groupCount = labels.size
                        val startX = 0f
                        val groupWidth = barData.getGroupWidth(groupSpace, barSpace)

                        val xAxis = barChart.xAxis
                        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.granularity = 1f
                        xAxis.isGranularityEnabled = true
                        xAxis.setDrawGridLines(false)
                        xAxis.labelRotationAngle = -45f
                        xAxis.setCenterAxisLabels(true) // ✅ Must be true for groupBars
                        xAxis.axisMinimum = startX
                        xAxis.axisMaximum = startX + groupWidth * groupCount

                        barChart.setFitBars(false)
                        barChart.groupBars(startX, groupSpace, barSpace)
                        barChart.axisRight.isEnabled = false
                        barChart.description.isEnabled = false
                        barChart.setExtraBottomOffset(25f)

                        barChart.invalidate()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}