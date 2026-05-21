package com.example.universitynewsapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.universitynewsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private lateinit var tvProfile: TextView
    private lateinit var tvUserPosts: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        tvProfile = findViewById(R.id.tvProfile)
        tvUserPosts = findViewById(R.id.tvUserPosts)

        val userId = intent.getIntExtra("userId", 1)

        loadUser(userId)
        loadUserPosts(userId)
    }

    private fun loadUser(userId: Int) {
        lifecycleScope.launch {
            try {
                val user = RetrofitClient.instance.getUserById(userId)

                tvProfile.text =
                    "${user.name}\n@${user.username}\n${user.email}\n${user.phone}\n${user.website}\n${user.company.name}\n${user.company.catchPhrase}"

            } catch (e: Exception) {
                Toast.makeText(this@UserProfileActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserPosts(userId: Int) {
        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.instance.getPostsByUser(userId)

                val text = StringBuilder()
                text.append("Posts by this user\n\n")

                for (post in posts) {
                    text.append("• ${post.title}\n\n")
                }

                tvUserPosts.text = text.toString()

            } catch (e: Exception) {
                Toast.makeText(this@UserProfileActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}