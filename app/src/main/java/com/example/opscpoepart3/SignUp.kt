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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")


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

            auth.signInWithEmailAndPassword(username, confirmPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = auth.currentUser?.uid
                        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userID!!)
                        Toast.makeText(this, "Login Sucessful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LandingPage::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
        }
        val goToMainPage = findViewById<Button>(R.id.btnReturn)
        goToMainPage?.setOnClickListener{
            val intent = Intent( this,MainActivity::class.java)
            startActivity(intent)
        }
     }
    }
}





