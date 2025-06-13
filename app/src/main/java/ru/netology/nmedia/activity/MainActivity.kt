package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractorListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val intent2 = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(intent2)
            }

            override fun onRemove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun viewVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
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

        val newPostLauncherEdit = registerForActivityResult(EditPostResultContract()) { content ->
            content ?: return@registerForActivityResult
            viewModel.save(content)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                with(binding) {
                    newPostLauncherEdit.launch(post.content)
                }
            }
        }

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { content ->
            content ?: return@registerForActivityResult
            viewModel.save(content)
        }


        binding.fab.setOnClickListener {
            viewModel.closeEdit()
            newPostLauncher.launch()
        }

    }
}