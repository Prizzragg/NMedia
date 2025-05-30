package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likes = 999,
        reposts =1099999,
        share = 12113,
        likedByMe = false
    )
    var countLikes = post.likes
    var countReposts = post.reposts

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        if (post.likedByMe) {
            countLikes--
            post = post.copy(likedByMe = !post.likedByMe, likes = countLikes)
        } else {
            countLikes++
            post = post.copy(likedByMe = !post.likedByMe, likes = countLikes)
        }
        data.value = post
    }

    override fun repost() {
        countReposts++
        post = post.copy(reposts = countReposts)
        data.value = post
    }

}