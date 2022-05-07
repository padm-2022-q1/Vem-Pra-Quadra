package com.reis.vinicius.vempraquadra.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.ContentDialogNewPostBinding

class PostDialogFragment : DialogFragment() {
    private lateinit var binding: ContentDialogNewPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContentDialogNewPostBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun bindFabEvents(){
        binding.btnPostCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnPostSave.setOnClickListener {
            // TODO: Implement save post button behaviour
        }
    }
}