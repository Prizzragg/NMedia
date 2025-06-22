package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.FeedFragment.Companion.textArgs
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        arguments?.textArgs.let(binding.edit::setText)
        binding.cancelEdit.setOnClickListener {
            viewModel.closeEdit()
            findNavController().navigateUp()
        }
        binding.save.setOnClickListener {
            val content = binding.edit.text.toString()
            viewModel.save(content)
            findNavController().navigateUp()
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback {
            viewModel.closeEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }
}