package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefImpl(context: Context) : PostRepository {

    private var pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var index = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        pref.getString(POSTS_KEY, null)?.let { json ->
            posts = gson.fromJson(json, type)
            index = (posts.maxOfOrNull { post -> post.id } ?: 0) + 1
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
                )
            } else {
                post
            }
        }
    }

    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    reposts = post.reposts + 1
                )
            } else {
                post
            }
        }
    }

    override fun remove(id: Long) {
        posts = posts.filter {
            it.id != id
        }
    }

    override fun save(post: Post) {
        if (post.id != 0L) {
            posts = posts.map {
                if (post.id == it.id) {
                    it.copy(content = post.content)
                } else it
            }
        } else {
            posts = listOf(
                post.copy(
                    id = index++,
                    author = "Me",
                    published = "Now",
                    likedByMe = false
                )
            ) + posts
        }
        return
    }

    private fun sync() {
        pref.edit {
            putString(POSTS_KEY, gson.toJson(posts, type))
        }
    }

    companion object {
        private const val SHARED_PREF_NAME = "repo"
        private const val POSTS_KEY = "posts"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}