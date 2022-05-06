package com.reis.vinicius.vempraquadra.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentHomeBinding
import com.reis.vinicius.vempraquadra.view.chat.ChatListFragment
import com.reis.vinicius.vempraquadra.view.feed.FeedFragment
import com.reis.vinicius.vempraquadra.view.match.MatchesFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout: DrawerLayout = binding.drawerLayoutHome
        val navView: NavigationView = binding.navView
        val navController = findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        parentFragmentManager.commit {
            replace(binding.fragmentContainerHome.id, FeedFragment())
        }

        bindBottomNavEvents()
        bindLogoutEvent()
    }

    private fun bindBottomNavEvents() {
        binding.bottomNavMainMenu.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId){
                R.id.item_main_menu_feed -> FeedFragment()
                R.id.item_main_menu_chat -> ChatListFragment()
                R.id.item_main_menu_matches -> MatchesFragment()
                else -> FeedFragment()
            }

            parentFragmentManager.commit {
                replace(binding.fragmentContainerHome.id, fragment)
            }
            true
        }
    }

    private fun bindLogoutEvent(){
        val btnLogout = binding.navView.getHeaderView(0).findViewById<ImageButton>(R.id.btn_user_logout)

        btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(HomeFragmentDirections.openLogin(), navOptions {
                popUpTo(R.id.destination_login)
            })
        }
    }
}