package com.example.universitynewsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universitynewsapp.model.Post
import com.example.universitynewsapp.model.User
import com.example.universitynewsapp.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var tvError: TextView
    private lateinit var btnRetry: Button
    private lateinit var btnPosts: Button
    private lateinit var btnUsers: Button
    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    private val postList = ArrayList<Post>()
    private val allPosts = ArrayList<Post>()
    private val userList = ArrayList<User>()

    private lateinit var postAdapter: PostAdapter
    private lateinit var userAdapter: UserAdapter

    private var currentTab = "posts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
        tvError = findViewById(R.id.tvError)
        btnRetry = findViewById(R.id.btnRetry)
        btnPosts = findViewById(R.id.btnPosts)
        btnUsers = findViewById(R.id.btnUsers)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        postAdapter = PostAdapter(postList) { post ->
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("post", post)
            startActivity(intent)
        }

        userAdapter = UserAdapter(userList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        loadPosts()

        btnPosts.setOnClickListener {
            currentTab = "posts"
            searchView.visibility = View.VISIBLE
            recyclerView.adapter = postAdapter
            filterPosts(searchView.query.toString())
        }

        btnUsers.setOnClickListener {
            currentTab = "users"
            searchView.visibility = View.GONE
            recyclerView.adapter = userAdapter
            loadUsers()
        }

        btnRetry.setOnClickListener {
            if (currentTab == "posts") loadPosts() else loadUsers()
        }

        swipeRefresh.setOnRefreshListener {
            if (currentTab == "posts") loadPosts() else loadUsers()
        }

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    filterPosts(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterPosts(newText ?: "")
                    return true
                }
            }
        )
    }

    private fun loadPosts() {
        showLoading()

        lifecycleScope.launch {
            try {
                val posts = RetrofitClient.instance.getAllPosts()

                allPosts.clear()
                allPosts.addAll(posts)

                postList.clear()
                postList.addAll(posts)

                postAdapter.notifyDataSetChanged()
                showSuccess()

            } catch (e: Exception) {
                showError(e.message ?: "Something went wrong")
            }
        }
    }

    private fun loadUsers() {
        showLoading()

        lifecycleScope.launch {
            try {
                val users = RetrofitClient.instance.getAllUsers()

                userList.clear()
                userList.addAll(users)

                userAdapter.notifyDataSetChanged()
                showSuccess()

            } catch (e: Exception) {
                showError(e.message ?: "Something went wrong")
            }
        }
    }

    private fun filterPosts(query: String) {
        postList.clear()

        if (query.isEmpty()) {
            postList.addAll(allPosts)
        } else {
            for (post in allPosts) {
                if (post.title.contains(query, ignoreCase = true)) {
                    postList.add(post)
                }
            }
        }

        postAdapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE
        btnRetry.visibility = View.GONE
    }

    private fun showSuccess() {
        progressBar.visibility = View.GONE
        tvError.visibility = View.GONE
        btnRetry.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        swipeRefresh.isRefreshing = false
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        btnRetry.visibility = View.VISIBLE
        tvError.text = message
        swipeRefresh.isRefreshing = false
    }
}