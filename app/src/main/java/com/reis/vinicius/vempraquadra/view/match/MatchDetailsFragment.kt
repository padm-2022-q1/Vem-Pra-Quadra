package com.reis.vinicius.vempraquadra.view.match

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchDetailsBinding
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.MatchViewModel

class MatchDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailsBinding
    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchDetailsFragmentArgs by navArgs()
    private val matchCache = MutableLiveData<Match>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.matchDetailsLayout.visibility = View.INVISIBLE
        viewModel.getById(args.matchId).observe(viewLifecycleOwner) { status ->
            when (status) {
                is MainViewModel.Status.Loading -> toggleLayout(false)
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", "Failed to show match details", status.e)
                    showSnackbar(status.e.message)
                }
                is MainViewModel.Status.Success -> {
                    val match = (status.result as MainViewModel.Result.Data<Match>).obj

                    matchCache.value = match
                }
            }
        }
    }

    private fun fillDetails(){
        matchCache.observe(viewLifecycleOwner) { match ->
            binding.textMatchDetailsName.text = match.name
            binding.textMatchDetailsAddress.text = match.
        }
    }

    private fun toggleLayout(show: Boolean) {
        binding.matchDetailsLayout.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.progressMatchDetails.visibility = if (!show) View.VISIBLE else View.INVISIBLE
    }

    private fun showSnackbar(message: String?) {
        Snackbar.make(binding.root, message ?: "Please, try again later",
            Snackbar.LENGTH_LONG).show()
    }
}