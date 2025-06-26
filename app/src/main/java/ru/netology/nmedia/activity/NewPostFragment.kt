package ru.netology.nmedia.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.FeedFragment.Companion.textArgs
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .getString("text", null)?.let {
                binding.edit.setText(it)
            }
        binding.save.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                findNavController().navigateUp()
            } else {
                val content = binding.edit.text.toString()
                val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                pref.edit().apply() {
                    putString("text", null)
                    commit()
                }
                viewModel.save(content)
            }
            findNavController().navigateUp()
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback {
            val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            pref.edit().apply() {
                putString("text", binding.edit.text.toString())
                commit()
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}