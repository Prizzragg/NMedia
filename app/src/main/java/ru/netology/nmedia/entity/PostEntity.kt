package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val reposts: Int = 0,
    val views: Int = 1546456,
    val likedByMe: Boolean = false,
    val video: String? = null
) {
    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likes = likes,
        reposts = reposts,
        views = views,
        likedByMe = likedByMe,
        video = video
    )

    companion object {
        fun fromDto(post: Post): PostEntity = post.run {
            PostEntity(
                id = id,
                author = author,
                content = content,
                published = published,
                likes = likes,
                reposts = reposts,
                views = views,
                likedByMe = likedByMe,
                video = video
            )
        }
    }
}

