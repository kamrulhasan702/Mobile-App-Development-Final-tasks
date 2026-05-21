package com.example.universitynewsapp.model

import java.io.Serializable

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) : Serializable