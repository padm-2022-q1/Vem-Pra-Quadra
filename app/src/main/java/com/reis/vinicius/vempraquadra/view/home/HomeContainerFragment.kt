package com.reis.vinicius.vempraquadra.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentHomeContainerBinding

class HomeContainerFragment : Fragment() {
    private lateinit var binding: FragmentHomeContainerBinding
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeContainerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBar = binding.appBarMainMenu
        val drawerLayout = binding.drawerLayoutHome
        val navView = binding.navView

        navView.setupWithNavController(getNavController())
        appBar.setupWithNavController(getNavController())

        bindLogoutEvent()
    }

    private fun bindLogoutEvent(){
        val btnLogout = binding.navView.getHeaderView(0).findViewById<ImageButton>(R.id.btn_user_logout)

        btnLogout.setOnClickListener {
            auth.signOut()
            getNavController().navigate(HomeContainerFragmentDirections.logout(), navOptions {
                popUpTo(R.id.destination_login)
            })
        }
    }

    private fun getNavController() =
        (childFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment).navController

}