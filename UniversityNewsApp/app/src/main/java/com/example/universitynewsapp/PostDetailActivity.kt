package com.example.universitynewsapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.universitynewsapp.model.Post
import com.example.universitynewsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class PostDetailActivity : AppCompatActivity() {

    private lateinit var tvPostTitle: TextView
    private lateinit var tvPostBody: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvComments: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        tvPostTitle = findViewById(R.id.tvPostTitle)
        tvPostBody = findViewById(R.id.tvPostBody)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvComments = findViewById(R.id.tvComments)

        val post = intent.getSerializableExtra("post") as Post

        tvPostTitle.text = post.title
        tvPostBody.text = post.body

        loadAuthor(post.userId)
        loadComments(post.id)
    }

    private fun loadAuthor(userId: Int) {
        lifecycleScope.launch {
            try {
                val user = RetrofitClient.instance.getUserById(userId)

                tvAuthor.text =
                    "Author: ${user.name}\nEmail: ${user.email}\nCompany: ${user.company.name}"

            } catch (e: Exception) {
                Toast.makeText(this@PostDetailActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadComments(postId: Int) {
        lifecycleScope.launch {
            try {
                val comments = RetrofitClient.instance.getCommentsByPost(postId)

                val text = StringBuilder()
                text.append("Comments\n\n")

                for (comment in comments) {
                    text.append("${comment.name}\n")
                    text.append("${comment.email}\n")
                    text.append("${comment.body}\n\n")
                }

                tvComments.text = text.toString()

            } catch (e: Exception) {
                Toast.makeText(this@PostDetailActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}