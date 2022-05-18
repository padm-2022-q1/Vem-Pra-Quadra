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
import com.reis.vinicius.vempraquadra.view.match.MatchListFragment

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var appBar: MaterialToolbar
    private var currentMenuItemId = R.id.item_main_menu_matches

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
        loadTab(currentMenuItemId)
    }

    private fun bindBottomNavEvents() {
        binding.bottomNavMainMenu.setOnItemSelectedListener { item ->
            currentMenuItemId = item.itemId
            loadTab(currentMenuItemId)
            true
        }
    }

    private fun loadTab(itemId: Int){
        val fragment = when (itemId){
            R.id.item_main_menu_matches -> MatchListFragment()
            R.id.item_main_menu_chat -> ChatListFragment()
            R.id.item_main_menu_courts -> CourtListFragment()
            else -> MatchListFragment()
        }

        parentFragmentManager.commit {
            replace(binding.mainMenuFragmentContainer.id, fragment)
        }
    }
}