package com.example.opscpoepart3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        auth = FirebaseAuth.getInstance()

        val goToSignUp = findViewById<Button>(R.id.btnsignup)
        goToSignUp.setOnClickListener {

            val txtName = findViewById<EditText>(R.id.edtName)
            val txtEmail = findViewById<EditText>(R.id.edtEmail)
            val txtNumber = findViewById<EditText>(R.id.edtPhoneNumber)
            val txtUsername = findViewById<EditText>(R.id.edtUsername)
            val txtPassword = findViewById<EditText>(R.id.edtPassword)
            val txtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)

            // extracting from the edit text
            val NameandSurname = txtName.text.toString();
            val email = txtEmail.text.toString();
            val phoneNumber = txtNumber.text.toString();
            val username = txtUsername.text.toString();
            val password = txtPassword.text.toString();
            val confirmPassword = txtConfirmPassword.text.toString()

            if (NameandSurname.isEmpty()) {
                txtName.error = "Enter Name and Surname";
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                txtEmail.error = "Enter Email"
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                txtNumber.error = "Enter Phone Number"
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                txtUsername.error = "Enter Username"
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                txtPassword.error = "Enter password"
                return@setOnClickListener
            }

            if (confirmPassword != password) {
                txtConfirmPassword.error = "Enter correct password"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(username, confirmPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignUp::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            val goToMainPage = findViewById<Button>(R.id.btnBack)
            goToMainPage?.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        }
    }
