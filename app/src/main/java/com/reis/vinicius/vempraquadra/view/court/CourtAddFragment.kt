package com.reis.vinicius.vempraquadra.view.court

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentCourtAddBinding
import com.reis.vinicius.vempraquadra.model.court.Court
import com.reis.vinicius.vempraquadra.viewModel.CourtViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import kotlinx.coroutines.launch

class CourtAddFragment : Fragment() {
    private lateinit var binding: FragmentCourtAddBinding
    private val viewModel: CourtViewModel by activityViewModels()
    private lateinit var appBar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourtAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appBar = requireActivity().findViewById<MaterialToolbar>(R.id.app_bar_main_menu)

        appBar.inflateMenu(R.menu.menu_court_add)

        lifecycle.coroutineScope.launch {
            viewModel.isSubmitEnabled.collect { value ->
                appBar.menu.getItem(R.id.menu_item_court_save).isEnabled = value
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setHasOptionsMenu(true)

        bindFormEvents()
        bindAppBarItemEvent()
    }

    private fun bindAppBarItemEvent(){
        appBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_court_save -> {
                    viewModel.insert(parseCourtData()).observe(viewLifecycleOwner) { status ->
                        when (status) {
                            is MainViewModel.Status.Loading -> item.isEnabled = false
                            is MainViewModel.Status.Failure -> {
                                item.isEnabled = true
                                Log.e("FRAGMENT", "Failed to save court", status.e)
                                Snackbar.make(binding.root, "Failed to save court. Please, try again later",
                                    Snackbar.LENGTH_LONG).show()
                            }
                            is MainViewModel.Status.Success -> {
                                item.isEnabled = true
                                getNavController().popBackStack()
                            }
                        }
                    }
                }
            }

            true
        }
    }

    private fun parseCourtData() = Court(
        id = 0,
        name = binding.textInputCourtName.text.toString(),
        address = binding.textInputCourtAddress.text.toString()
    )

    private fun bindFormEvents(){
        binding.textInputCourtName.addTextChangedListener { text ->
            viewModel.setName(text.toString())

            binding.textLayoutCourtName.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.textInputCourtAddress.addTextChangedListener { text ->
            viewModel.setAddress(text.toString())

            binding.textLayoutCourtAddress.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }
    }

    private fun getNavController() =
        requireActivity().findNavController(R.id.nav_host_home)
}