package com.reis.vinicius.vempraquadra.view.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentSignupBinding
import com.reis.vinicius.vempraquadra.model.user.UserData
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

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
        bindFormValidation()
        bindTextValidations()
        bindBirthDatePicker()
        bindSaveFabEvents()
    }

    private fun bindSaveFabEvents() {
        binding.btnSignupSave.setOnClickListener {
            val (email, password, userData) = parseUserData()

            auth.createUserWithEmailAndPassword(email as String, password as String)
                .addOnCanceledListener {
                    Snackbar.make(binding.root, "Sign up cancelled", Snackbar.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Log.e("FRAGMENT", "Failed to add user to Firebase", e)
                    Snackbar.make(binding.root, "Failed to sign you up", Snackbar.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    viewModel.insert(userData as UserData).observe(viewLifecycleOwner) { status ->
                        when (status) {
                            is MainViewModel.Status.Success ->
                                findNavController().navigate(LoginFragmentDirections.login()
                                    , navOptions { popUpTo(R.id.destination_home_container) })
                            is MainViewModel.Status.Loading -> {}
                            is MainViewModel.Status.Failure -> {
                                Log.e("FRAGMENT", "Failed to save user data", status.e)
                                Snackbar.make(binding.root, "Failed to save user data"
                                    , Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }
    }

    private fun loadGenderDropdown() {
        val genders = listOf(R.string.male, R.string.female, R.string.other).map { getString(it) }
        val adapter = ArrayAdapter(
            requireContext(), R.layout.dropdown_list_item,
            R.id.text_item_name, genders
        )

        (binding.inputLayoutSignupGender.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun parseUserData(): Array<Any> = arrayOf(
        binding.inputTextSignupEmail.text.toString(),
        binding.inputTextSignupPasswordConfirm.text.toString(),
        UserData(
            "",
            binding.inputTextSignupName.text.toString(),
            binding.inputTextSignupUsername.text.toString(),
            binding.autoCompleteGender.toString(),
            Date()
        )
    )

    private fun bindFormValidation() {
        lifecycle.coroutineScope.launch {
            viewModel.isSubmitEnabled.collect { value ->
                binding.btnSignupSave.isEnabled = value
            }
        }
    }

    private fun bindBirthDatePicker(){
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.action_select_birth_text).build()

        binding.inputTextSignupBirth.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, null)
        }

        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it

            binding.inputTextSignupBirth.setText(SimpleDateFormat("dd/MM/yyyy", Locale.US)
                .format(calendar.time))
        }
    }

    private fun bindTextValidations() {
        binding.inputTextSignupName.addTextChangedListener { text ->
            viewModel.setName(text.toString())

            binding.inputTextSignupName.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.inputTextSignupUsername.addTextChangedListener { text ->
            viewModel.setUsername(text.toString())

            binding.inputTextSignupUsername.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.inputTextSignupEmail.addTextChangedListener { text ->
            viewModel.setEmail(text.toString())

            binding.inputTextSignupEmail.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.inputTextSignupPassword.addTextChangedListener { text ->
            viewModel.setPassword(text.toString())

            binding.inputTextSignupPassword.error = if (text.isNullOrEmpty())
                getString(R.string.warning_required_field) else null
        }

        binding.inputTextSignupPasswordConfirm.addTextChangedListener { text ->
            viewModel.setPassConfirm(text.toString())

            if (text.isNullOrEmpty())
                binding.inputTextSignupPasswordConfirm.error = getString(R.string.warning_required_field)
            else if (!binding.inputTextSignupPassword.text.isNullOrEmpty()
                && text.trim().toString() != binding.inputTextSignupPassword.text.toString().trim())
                binding.inputTextSignupUsername.error = null
        }
    }
}