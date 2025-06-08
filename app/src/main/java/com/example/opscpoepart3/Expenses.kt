package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Expenses : AppCompatActivity() {

    private var selectedItem: String ?= null
    private lateinit var database: DatabaseReference

    private val picId = 123
    private var capturedPhoto: Bitmap? = null
    private lateinit var imageView: ImageView

    @SuppressLint("MissingInflatedId", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance().getReference("Expenses")

        val dateSelection = findViewById<Button>(R.id.btn_Date)
        val dateDisplay = findViewById<TextView>(R.id.txt_Date_Display)
        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        dateSelection.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                dateDisplay.text = formattedDate
            }, myYear, myMonth, myDay)
            datePickerDialog.show()
        }

        val spinner = findViewById<Spinner>(R.id.spinner3)

        val items = listOf(
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

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ){
                selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val addImage = findViewById<Button>(R.id.btn_Add_Image)
        imageView = findViewById<ImageView>(R.id.img_Image2)

        addImage.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, picId)
        }

        val saveExpense = findViewById<Button>(R.id.btn_Save_Expense)

        //val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        saveExpense.setOnClickListener {
            val txtExpenseAmount = findViewById<EditText>(R.id.edtAmount)
            val txtExpenseDescription = findViewById<EditText>(R.id.edtDescription)

            val expenseAmount = txtExpenseAmount.text.toString()
            val amount = expenseAmount.toDoubleOrNull()
            val expenseDescription = txtExpenseDescription.text.toString()
            val dateString = dateDisplay.text.toString()

            if (expenseDescription.isEmpty()){
                Toast.makeText(this, "Please enter a description.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount == null || amount == 0.0){
                Toast.makeText(this, "Please enter a valid amount greater than zero.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = selectedItem ?: ""
            if (category == "Select category" || category.isEmpty()){
                Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateString.isEmpty()){
                Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val parsedDate = try {
                inputFormat.parse(dateString)
            } catch (e: Exception) {
                null
            }

            if (parsedDate == null) {
                Toast.makeText(this, "Date format is invalid.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formattedDateForDB = outputFormat.format(parsedDate)


            if(dateString.isNotEmpty() && category.isNotEmpty() && expenseDescription.isNotEmpty() && expenseAmount.isNotEmpty())
            {
                val expenseId = database.push().key
                val expenseData = ExpensesData(formattedDateForDB,category,expenseDescription,expenseAmount)

                if(expenseId != null){
                    database.child(expenseId).setValue(expenseData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Expense saved successfully!",Toast.LENGTH_SHORT).show()
                            txtExpenseAmount.text.clear()
                            txtExpenseDescription.text.clear()
                            dateDisplay.text = ""
                            spinner.setSelection(0)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"Failed to add expense", Toast.LENGTH_SHORT)
                                .show()
                        }
                }

            }else{
                Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show()
            }
        }

        val viewExpenses = findViewById<Button>(R.id.btnViewExpenses)
        viewExpenses.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
        }

        val returnToLanding = findViewById<Button>(R.id.btnReturn4)
        returnToLanding.setOnClickListener {
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == picId && resultCode == RESULT_OK) {
            val photo = data?.extras?.get("data") as? Bitmap
            if (photo != null) {
                capturedPhoto = photo
                imageView.setImageBitmap(photo)
                imageView.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }


        }
    }

}