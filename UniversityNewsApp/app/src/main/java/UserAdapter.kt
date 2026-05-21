package com.example.universitynewsapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universitynewsapp.model.User

class UserAdapter(
    private val userList: ArrayList<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.tvName.text = user.name
        holder.tvEmail.text = user.email
        holder.tvAvatar.text = user.name.take(2).uppercase()

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserProfileActivity::class.java)
            intent.putExtra("userId", user.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size
}