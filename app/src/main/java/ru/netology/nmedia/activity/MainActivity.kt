package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Counter
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

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
        viewModel.data.observe(this) { post ->
            with(binding) {
                val counter = Counter
                author.text = post.author
                published.text = post.published
                content.text = post.content
                numberOfReposts.text = counter.shortNote(post.reposts)
                numberOfLikes.text = counter.shortNote(post.likes)
                numberOfViews.text = counter.shortNote(post.share)
                if (post.likedByMe) {
                    likes.setImageResource(R.drawable.baseline_favorite_border_red)
                }
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_border_red
                    else R.drawable.baseline_favorite_border_24
                )
            }
            binding.likes.setOnClickListener {
                viewModel.like()
            }
            binding.reposts.setOnClickListener {
                viewModel.repost()
            }
        }
    }
}