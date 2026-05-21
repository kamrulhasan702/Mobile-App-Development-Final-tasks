package com.example.studentprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var imgProfile: ImageView
    lateinit var tvHomeTitle: TextView
    lateinit var tvUserInfo: TextView
    lateinit var etNewPassword: EditText
    lateinit var etConfirmNewPassword: EditText

    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {

                imgProfile.setImageURI(uri)

                getSharedPreferences(
                    "ProfileImage",
                    MODE_PRIVATE
                ).edit()
                    .putString(
                        "IMAGE_URI",
                        uri.toString()
                    )
                    .apply()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        imgProfile = findViewById(R.id.imgProfile)
        tvHomeTitle = findViewById(R.id.tvHomeTitle)
        tvUserInfo = findViewById(R.id.tvUserInfo)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)

        findViewById<Button>(R.id.btnBackHome).setOnClickListener {
            finish()
        }

        imgProfile.setOnClickListener {
            imagePicker.launch("image/*")
        }

        loadSavedImage()

        showUserInfo()

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser()
        }

        findViewById<Button>(R.id.btnUpdatePassword).setOnClickListener {
            updatePassword()
        }

        findViewById<Button>(R.id.btnDeleteAccount).setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun loadSavedImage() {
        val savedImage =
            getSharedPreferences(
                "ProfileImage",
                MODE_PRIVATE
            ).getString(
                "IMAGE_URI",
                null
            )

        if (savedImage != null) {
            imgProfile.setImageURI(
                Uri.parse(savedImage)
            )
        }
    }

    private fun showUserInfo() {
        val user = auth.currentUser

        if (user == null) {
            goToLogin()
            return
        }

        val name = user.displayName ?: "Student"
        val email = user.email ?: "No email"
        val uid = user.uid.take(8)

        val creationTime =
            user.metadata?.creationTimestamp ?: 0

        val date = SimpleDateFormat(
            "dd MMM yyyy hh:mm a",
            Locale.getDefault()
        ).format(Date(creationTime))

        tvHomeTitle.text = "Welcome, $name"

        tvUserInfo.text =
            "Email: $email\n\nUID: $uid\n\nAccount Created: $date"
    }

    private fun updatePassword() {
        val newPassword =
            etNewPassword.text.toString().trim()

        val confirmPassword =
            etConfirmNewPassword.text.toString().trim()

        if (newPassword.length < 8) {
            etNewPassword.error =
                "Password must be at least 8 characters"
            return
        }

        if (newPassword != confirmPassword) {
            etConfirmNewPassword.error =
                "Password does not match"
            return
        }

        auth.currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Password updated",
                        Toast.LENGTH_SHORT
                    ).show()

                    etNewPassword.setText("")
                    etConfirmNewPassword.setText("")

                } else {

                    Snackbar.make(
                        etNewPassword,
                        task.exception?.message
                            ?: "Password update failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage(
                "Are you sure you want to delete your account?"
            )
            .setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Account deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    goToLogin()

                } else {

                    Snackbar.make(
                        tvUserInfo,
                        task.exception?.message
                            ?: "Delete failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun logoutUser() {
        auth.signOut()
        goToLogin()
    }

    private fun goToLogin() {
        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}