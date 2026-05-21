package com.example.studentcoursemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcoursemanager.databinding.ItemCourseBinding

class CourseAdapter(
    private val courseList: ArrayList<Course>,
    private val onEditClick: (Course) -> Unit,
    private val onDeleteClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCourseBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {

        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CourseViewHolder,
        position: Int
    ) {

        val course = courseList[position]

        holder.binding.tvCourseName.text = course.name
        holder.binding.tvCourseCode.text = course.code
        holder.binding.tvInstructor.text =
            "Instructor: ${course.instructor}"

        holder.binding.tvCredit.text =
            "Credits: ${course.creditHours}"

        holder.binding.tvSchedule.text =
            "Schedule: ${course.schedule}"

        holder.binding.btnEdit.setOnClickListener {
            onEditClick(course)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(course)
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }
}