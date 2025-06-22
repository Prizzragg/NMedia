package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.textArgs
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Counter
import ru.netology.nmedia.viewmodel.PostViewModel

class SinglePostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentSinglePostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId: Long = arguments?.textArgs?.toLong() ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding.post) {
                val counter = Counter
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.apply {
                    isChecked = post.likedByMe
                    text = counter.shortNote(post.likes).toString()
                }
                reposts.text = counter.shortNote(post.reposts).toString()
                views.text = counter.shortNote(post.views).toString()
                likes.setOnClickListener {
                    viewModel.like(post.id)
                }
                reposts.setOnClickListener {
                    viewModel.repost(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val intent2 =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(intent2)
                }

                if (post.video != null) {
                    videoImage.visibility = View.VISIBLE
                } else {
                    videoImage.visibility = View.GONE
                }
                videoImage.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intent)
                }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.option_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.remove(post.id)
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                with(binding) {
                    findNavController().navigate(
                        R.id.action_singlePostFragment_to_editPostFragment,
                        Bundle().apply {
                            textArgs = post.content
                        })
                }
            }
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback {
            viewModel.closeEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }
}