package ru.netology.nmedia.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractorListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = PostAdapter(object : OnInteractorListener{
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val isNew = posts.size != adapter.itemCount
            adapter.submitList(posts) {
                if (isNew) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.content.requestFocus()
                binding.content.setText(post.content)
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Content cant be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }
    }
}