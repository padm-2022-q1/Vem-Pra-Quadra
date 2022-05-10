package com.reis.vinicius.vempraquadra.view.court

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.ContentDialogNewCourtBinding
import com.reis.vinicius.vempraquadra.model.court.Court
import com.reis.vinicius.vempraquadra.viewModel.CourtViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CourtAddDialogFragment(layoutId: Int) : DialogFragment(layoutId) {
    private lateinit var binding: ContentDialogNewCourtBinding
    private val viewModel: CourtViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContentDialogNewCourtBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        bindFormEvents()
        bindButtonsEvents()
    }

    override fun dismiss() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun parseCourtData() = Court(
        id = 0,
        name = binding.textInputCourtName.text.toString(),
        address = binding.textInputCourtAddress.text.toString()
    )

    private fun bindButtonsEvents(){
        binding.btnCourtCancel.setOnClickListener {
            this.dismiss()
        }

        binding.btnCourtSave.setOnClickListener {
            viewModel.insert(parseCourtData()).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> binding.btnCourtSave.isEnabled = false
                    is MainViewModel.Status.Failure -> {
                        Log.e("FRAGMENT", "Failed to save court", status.e)
                        Snackbar.make(binding.root, "Failed to save court. Please, try again later",
                            Snackbar.LENGTH_LONG).show()
                    }
                    is MainViewModel.Status.Success -> {
                        this.dismiss()
                    }
                }
            }
        }
    }

    private fun bindFormEvents(){
        lifecycle.coroutineScope.launch {
            viewModel.isSubmitEnabled.collect { value ->
                binding.btnCourtSave.isEnabled = value
            }
        }

        binding.textInputCourtName.addTextChangedListener { text ->
            viewModel.setName(text.toString())

            if (text.isNullOrEmpty())
                binding.textLayoutCourtName.error = "Required field"
            else
                binding.textLayoutCourtName.error = null
        }

        binding.textInputCourtAddress.addTextChangedListener { text ->
            viewModel.setAddress(text.toString())

            if (text.isNullOrEmpty())
                binding.textLayoutCourtAddress.error = "Required field"
            else
                binding.textLayoutCourtAddress.error = null
        }
    }
}