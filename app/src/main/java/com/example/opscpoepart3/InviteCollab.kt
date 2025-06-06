package com.example.opscpoepart3

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InviteCollab : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button

    private val roles = listOf("Viewer", "Editor", "Admin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_collab)

        emailInput = findViewById(R.id.edtEmailAddress)
        roleSpinner = findViewById(R.id.spinnerRole)
        messageInput = findViewById(R.id.edtMessage)
        sendButton = findViewById(R.id.btnSendInvite)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        sendButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()
            val message = messageInput.text.toString().trim()

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Enter a valid email"
                return@setOnClickListener
            }
            emailInput.text.clear()
            messageInput.text.clear()
            roleSpinner.setSelection(0)

            onGoalCompleted()
        }

        }
    private fun showBadge(badge: Approved){
        val badgeView =
            LayoutInflater.from(this).inflate(R.layout.activity_item_approved,null)
        val icon = badgeView.findViewById<ImageView>(R.id.ApprovedIcon)
        val title = badgeView.findViewById<TextView>(R.id.ApprovedTitle)
        val desc = badgeView.findViewById<TextView>(R.id.ApprovedDescription)

        icon.setImageResource(badge.imageResId)
        title.text = badge.title
        desc.text = badge.description

        AlertDialog.Builder(this).setTitle("You've sent an invite!")
            .setView(badgeView)
            .setPositiveButton("Awesome!"){dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun onGoalCompleted(){
        val badge = Approved(
            title = "Invite Sent!",
            description = "Collaborator will join you soon",
            imageResId = R.drawable.img
        )
        showBadge(badge)
    }

}



