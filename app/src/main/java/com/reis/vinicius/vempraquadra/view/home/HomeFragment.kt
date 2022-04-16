package com.reis.vinicius.vempraquadra.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentHomeBinding
import com.reis.vinicius.vempraquadra.view.chat.ChatListFragment
import com.reis.vinicius.vempraquadra.view.feed.FeedFragment
import com.reis.vinicius.vempraquadra.view.match.MatchesFragment

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        parentFragmentManager.commit {
            replace(binding.fragmentContainerHome.id, FeedFragment())
        }

        bindBottomNavEvents()
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
}