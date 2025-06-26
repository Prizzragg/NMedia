package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val reposts: Int = 0,
    val views: Int = 1546456,
    val likedByMe: Boolean = false,
    val video: String? = null
)