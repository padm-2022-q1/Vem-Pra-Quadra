package com.reis.vinicius.vempraquadra.view.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.actionBar?.hide()

        bindLoginEvents()
    }

    private fun bindLoginEvents(){
        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.inputTextLoginEmail.text.toString()
            val password = binding.inputTextLoginPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    findNavController().navigate(LoginFragmentDirections.login())
                } else {
                    Log.e("AUTH", "Failed to authenticate user", task.exception)
                    Snackbar.make(binding.root, "Failed to sign you in", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding.buttonLoginSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.openSignup())
        }
    }
}