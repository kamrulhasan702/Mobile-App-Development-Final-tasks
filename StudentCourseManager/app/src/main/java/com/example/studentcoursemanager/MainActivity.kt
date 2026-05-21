package com.example.studentcoursemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentcoursemanager.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var courseList: ArrayList<Course>
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        courseList = ArrayList()

        adapter = CourseAdapter(
            courseList,
            onEditClick = { course ->
                val intent = Intent(this, AddCourseActivity::class.java)
                intent.putExtra("course", course)
                startActivity(intent)
            },
            onDeleteClick = { course ->
                deleteCourse(course)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("courses")

        loadCourses()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }
    }

    private fun loadCourses() {
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()

                for (data in snapshot.children) {
                    val course = data.getValue(Course::class.java)
                    if (course != null) {
                        courseList.add(course)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun deleteCourse(course: Course) {
        database.child(course.id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Course Deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}