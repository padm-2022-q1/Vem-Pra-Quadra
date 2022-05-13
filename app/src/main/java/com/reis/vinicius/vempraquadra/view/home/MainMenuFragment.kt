package com.reis.vinicius.vempraquadra.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.appbar.MaterialToolbar
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMainMenuBinding
import com.reis.vinicius.vempraquadra.view.chat.ChatListFragment
import com.reis.vinicius.vempraquadra.view.court.CourtListFragment
import com.reis.vinicius.vempraquadra.view.feed.FeedFragment
import com.reis.vinicius.vempraquadra.view.match.MatchesFragment

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var appBar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBar = requireActivity().findViewById(R.id.app_bar_main_menu)

        appBar.menu.clear()

        bindBottomNavEvents()
        parentFragmentManager.commit {
            replace(binding.mainMenuFragmentContainer.id, FeedFragment())
        }
    }

    private fun bindBottomNavEvents() {
        binding.bottomNavMainMenu.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId){
                R.id.item_main_menu_feed -> FeedFragment()
                R.id.item_main_menu_chat -> ChatListFragment()
                R.id.item_main_menu_matches -> MatchesFragment()
                R.id.item_main_menu_courts -> CourtListFragment()
                else -> FeedFragment()
            }

            parentFragmentManager.commit {
                replace(binding.mainMenuFragmentContainer.id, fragment)
            }
            true
        }
    }
}