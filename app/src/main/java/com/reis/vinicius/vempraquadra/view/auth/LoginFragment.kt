package com.reis.vinicius.vempraquadra.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.reis.vinicius.vempraquadra.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

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

        bindLoginEvents()
    }

    private fun bindLoginEvents(){
        binding.buttonLoginLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.goToHome())
        }
    }
}