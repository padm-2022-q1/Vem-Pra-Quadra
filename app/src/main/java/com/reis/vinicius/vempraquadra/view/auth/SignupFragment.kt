package com.reis.vinicius.vempraquadra.view.auth

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentSignupBinding
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        loadGenderDropdown()
    }

    private fun loadGenderDropdown(){
        val genders = listOf(R.string.male, R.string.female, R.string.other).map { getString(it) }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_gender,
            R.id.text_gender_item, genders)

        (binding.inputSignupGender.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}