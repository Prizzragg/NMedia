package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Counter
import ru.netology.nmedia.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnRepostListener = (post: Post) -> Unit

class PostAdapter(private val onLikeListener: OnLikeListener, private val onRepostListener: OnRepostListener): ListAdapter<Post, PostViewHolder>(
    PostDiffCallBack
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onRepostListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(private val binding:CardPostBinding, private val onLikeListener: OnLikeListener, private val onRepostListener: OnRepostListener): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
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
        likes.setOnClickListener {
            onLikeListener(post)
        }
        reposts.setOnClickListener {
            onRepostListener(post)
        }
    }
}

object PostDiffCallBack: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem == newItem
    }
}