package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 999,
            reposts =1099999,
            share = 12113,
            likedByMe = false
        )
        with(binding) {
            fun shortNote(number: Int): String {
                return when (number) {
                    in 0..999 -> "$number"
                    in 1000..9999 -> {
                        val numberToString = (number/1000.0).toString()
                        if ((number % 1000) < 100) {
                            val result = "${number / 1000}K"
                            return result
                        }
                        val result = "${numberToString[0]}${numberToString[1]}${numberToString[2]}K"
                        result.toString()
                    }
                    in 10000..999999 -> "${number / 1000}K"
                    else -> {
                        val numberToString = (number/1000000.0).toString()
                        if ((number % 1000000) < 100000) {
                            val result = "${number / 1000000}M"
                            return result
                        }
                        val result = "${numberToString[0]}${numberToString[1]}${numberToString[2]}M"
                        result.toString()
                    }
                }
            }
            author.text = post.author
            published.text = post.published
            content.text = post.content
            numberOfReposts.text = shortNote(post.reposts)
            numberOfLikes.text = shortNote(post.likes)
            numberOfViews.text = shortNote(post.share)
            if (post.likedByMe) {
                likes.setImageResource(R.drawable.baseline_favorite_border_red)
            }
            likes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_border_red
                    else R.drawable.baseline_favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                numberOfLikes.text = shortNote(post.likes)
            }
            reposts.setOnClickListener {
                post.reposts++
                numberOfReposts.text = shortNote(post.reposts)
            }
        }
    }
}