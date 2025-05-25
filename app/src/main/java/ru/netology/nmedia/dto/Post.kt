package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    var reposts: Int = 0,
    var share: Int = 1546456,
    var likedByMe: Boolean = false
)