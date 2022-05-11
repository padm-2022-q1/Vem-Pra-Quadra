package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchAddBinding
import com.reis.vinicius.vempraquadra.model.court.Court
import com.reis.vinicius.vempraquadra.model.court.CourtDropdownListAdapter
import com.reis.vinicius.vempraquadra.viewModel.CourtViewModel
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel

class MatchAddFragment : Fragment() {
    private lateinit var binding: FragmentMatchAddBinding
    private val matchViewModel: MatchViewModel by activityViewModels()
    private val courtViewModel: CourtViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeCourtList()
    }

    private fun composeCourtList(){
        courtViewModel.getAll().observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> binding.inputLayoutMatchCourt.isEnabled = false
                is MainViewModel.Status.Failure -> {
                    binding.inputLayoutMatchCourt.isEnabled = true
                    Log.e("FRAGMENT", "Failed to get court list", status.e)
                    Snackbar.make(binding.root, "Failed to get court list",
                        Snackbar.LENGTH_LONG).show()
                    emptyList<Court>()
                }
                is MainViewModel.Status.Success -> {
                    val courts = (status.result as MainViewModel.Result.Data<List<Court>>).obj
                    val adapter = CourtDropdownListAdapter(requireContext(), R.layout.dropdown_list_item,
                        R.id.text_item_name, courts)

                    binding.autoCompleteMatchCourt.setAdapter(adapter)
                    binding.inputLayoutMatchCourt.isEnabled = true
                }
            }
        }
    }
}