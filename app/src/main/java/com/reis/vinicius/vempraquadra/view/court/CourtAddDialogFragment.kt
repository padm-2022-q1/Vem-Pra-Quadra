package com.reis.vinicius.vempraquadra.view.court

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.ContentDialogNewCourtBinding

class CourtAddDialogFragment(layoutId: Int) : DialogFragment(layoutId) {
    private lateinit var binding: ContentDialogNewCourtBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContentDialogNewCourtBinding.inflate(inflater, container, false)

        return binding.root
    }


}