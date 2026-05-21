package com.example.studentprofile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var etFullName: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etRegisterEmail)
        etPassword = findViewById(R.id.etRegisterPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        progress = findViewById(R.id.progressRegister)

        findViewById<Button>(R.id.btnBackRegister).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            registerUser()
        }

        findViewById<TextView>(R.id.tvGoLogin).setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (name.isEmpty()) {
            etFullName.error = "Full name required"
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Email required"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Invalid email"
            return
        }

        if (password.length < 8) {
            etPassword.error = "Password must be at least 8 characters"
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Password does not match"
            return
        }

        progress.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    val profileUpdates =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {

                            progress.visibility = View.GONE

                            val intent = Intent(this, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                            finish()
                        }

                } else {
                    progress.visibility = View.GONE

                    Snackbar.make(
                        etEmail,
                        task.exception?.message ?: "Registration failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }
}