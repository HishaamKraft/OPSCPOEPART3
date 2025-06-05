package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

//private lateinit var db: AppDatabase
//private lateinit var userDao: UserDao

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //db = AppDatabase.getDatabase(this)
        //userDao = db.UserDao()

        val verify = findViewById<Button>(R.id.btnSignInOfficial)
        verify.setOnClickListener {

            val txtUsername = findViewById<EditText>(R.id.edtUsernameSignUp)
            val txtConfirmPassword = findViewById<EditText>(R.id.edtPasswordSignUp)

            // extracting from the edit text
            val username = txtUsername.text.toString()
            val confirmPassword = txtConfirmPassword.text.toString()

            if (username.isEmpty()) {
                txtUsername.error = "Enter Username"
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                txtConfirmPassword.error = "Enter correct password"
                return@setOnClickListener
            }

            // In an Activity (ideally use ViewModel + coroutine)
            /*  lifecycleScope.launch {
                  val db = AppDatabase.getDatabase(applicationContext)
                  val user = db.UserDao().getUserByUsername(username)

                  if (user != null && user.confirmedpassword == confirmPassword) {
                      Toast.makeText(this@SignUp, "Login successful", Toast.LENGTH_SHORT).show()
                  } else {
                      Toast.makeText(this@SignUp, "Invalid email or password.", Toast.LENGTH_SHORT)
                          .show()
                  }

              }*/
            val intent = Intent(this, LandingPage::class.java)
            startActivity(intent)
        }
        val goToMainPage = findViewById<Button>(R.id.btnReturn)
        goToMainPage?.setOnClickListener{
            val intent = Intent( this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
