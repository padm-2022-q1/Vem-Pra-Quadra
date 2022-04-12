package com.reis.vinicius.vempraquadra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reis.vinicius.vempraquadra.databinding.FragmentLoginMainBinding

class LoginMainFragment : Fragment() {
    private lateinit var binding: FragmentLoginMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginMainBinding.inflate(inflater, container, false)

        return binding.root
    }
}