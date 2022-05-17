package com.reis.vinicius.vempraquadra

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.databinding.ActivityMainBinding
import com.reis.vinicius.vempraquadra.view.auth.LoginFragmentDirections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val auth = Firebase.auth

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null){
            (supportFragmentManager.findFragmentById(R.id.main_menu_fragment_container)
                    as? NavHostFragment)?.navController?.navigate(R.id.login_to_home)
        }
    }

    private fun getNavController(viewId: Int) = findNavController(viewId)
}