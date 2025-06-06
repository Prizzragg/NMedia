package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val emptyPost = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likedByMe = false
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data: LiveData<List<Post>> = repository.get()
    val edited = MutableLiveData(emptyPost)
    fun like(id: Long) = repository.like(id)
    fun repost(id: Long) = repository.repost(id)
    fun remove(id: Long) = repository.remove(id)
    fun changeContent(content: String) {
        val text = content.trim()
        edited.value?.let {
            if (it.content == text) {
                return
            } else {
                edited.value = it.copy(content = text)
            }
        }
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun closeEdit() {
        edited.value = emptyPost
    }

}