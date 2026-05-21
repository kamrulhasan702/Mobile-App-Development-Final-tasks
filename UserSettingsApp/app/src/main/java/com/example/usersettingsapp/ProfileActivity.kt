package com.example.usersettingsapp

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    lateinit var tvWelcome: TextView
    lateinit var etStudentId: EditText
    lateinit var etFullName: EditText
    lateinit var spinnerDepartment: Spinner
    lateinit var spinnerYear: Spinner
    lateinit var etEmail: EditText

    val departments = arrayOf(
        "CSE",
        "EEE",
        "BBA",
        "English",
        "Law"
    )

    val years = arrayOf(
        "1st Year",
        "2nd Year",
        "3rd Year",
        "4th Year"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        tvWelcome =
            findViewById(R.id.tvWelcome)

        etStudentId =
            findViewById(R.id.etStudentId)

        etFullName =
            findViewById(R.id.etFullName)

        spinnerDepartment =
            findViewById(R.id.spinnerDepartment)

        spinnerYear =
            findViewById(R.id.spinnerYear)

        etEmail =
            findViewById(R.id.etEmail)

        spinnerDepartment.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                departments
            )

        spinnerYear.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                years
            )

        loadProfile()

        findViewById<Button>(R.id.btnSaveProfile)
            .setOnClickListener {

                saveProfile()
            }
    }

    private fun saveProfile() {

        val prefs = getSharedPreferences(
            "ProfilePrefs",
            Context.MODE_PRIVATE
        )

        prefs.edit()
            .putString(
                "KEY_STUDENT_ID",
                etStudentId.text.toString()
            )
            .putString(
                "KEY_STUDENT_NAME",
                etFullName.text.toString()
            )
            .putString(
                "KEY_DEPARTMENT",
                spinnerDepartment.selectedItem.toString()
            )
            .putString(
                "KEY_YEAR",
                spinnerYear.selectedItem.toString()
            )
            .putString(
                "KEY_EMAIL",
                etEmail.text.toString()
            )
            .apply()

        tvWelcome.text =
            "Welcome back, ${etFullName.text}!"

        Toast.makeText(
            this,
            "Profile Saved",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadProfile() {

        val prefs = getSharedPreferences(
            "ProfilePrefs",
            Context.MODE_PRIVATE
        )

        val name = prefs.getString(
            "KEY_STUDENT_NAME",
            ""
        )

        if (!name.isNullOrEmpty()) {

            tvWelcome.text =
                "Welcome back, $name!"
        }

        etStudentId.setText(
            prefs.getString(
                "KEY_STUDENT_ID",
                ""
            )
        )

        etFullName.setText(name)

        etEmail.setText(
            prefs.getString(
                "KEY_EMAIL",
                ""
            )
        )

        val dept = prefs.getString(
            "KEY_DEPARTMENT",
            "CSE"
        )

        spinnerDepartment.setSelection(
            departments.indexOf(dept)
        )

        val year = prefs.getString(
            "KEY_YEAR",
            "1st Year"
        )

        spinnerYear.setSelection(
            years.indexOf(year)
        )
    }
}