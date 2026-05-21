package com.example.studentcoursemanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddCourseActivity : AppCompatActivity() {

    private lateinit var etCourseName: EditText
    private lateinit var etCourseCode: EditText
    private lateinit var etInstructor: EditText
    private lateinit var etCredit: EditText
    private lateinit var etSchedule: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_course)

        etCourseName = findViewById(R.id.etCourseName)
        etCourseCode = findViewById(R.id.etCourseCode)
        etInstructor = findViewById(R.id.etInstructor)
        etCredit = findViewById(R.id.etCredit)
        etSchedule = findViewById(R.id.etSchedule)

        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {

            val name = etCourseName.text.toString().trim()
            val code = etCourseCode.text.toString().trim()
            val instructor = etInstructor.text.toString().trim()
            val credit = etCredit.text.toString().trim()
            val schedule = etSchedule.text.toString().trim()

            if (
                name.isEmpty() ||
                code.isEmpty() ||
                instructor.isEmpty() ||
                credit.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val database =
                FirebaseDatabase.getInstance()
                    .getReference("courses")

            val courseId = database.push().key!!

            val course = Course(
                courseId,
                name,
                code,
                instructor,
                credit.toInt(),
                schedule,
                "",
                ""
            )

            database.child(courseId)
                .setValue(course)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Course Saved",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}