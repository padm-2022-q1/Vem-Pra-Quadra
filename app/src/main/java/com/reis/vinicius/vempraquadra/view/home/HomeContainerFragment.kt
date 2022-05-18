package com.reis.vinicius.vempraquadra.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentHomeContainerBinding
import com.reis.vinicius.vempraquadra.model.entity.UserData
import com.reis.vinicius.vempraquadra.viewModel.MainViewModel
import com.reis.vinicius.vempraquadra.viewModel.UserViewModel

class HomeContainerFragment : Fragment() {
    private lateinit var binding: FragmentHomeContainerBinding
    private val viewModel: UserViewModel by activityViewModels()
    private val auth = Firebase.auth
    private lateinit var userData: UserData

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

        if (auth.currentUser == null)
            getMainNavController().navigate(HomeContainerFragmentDirections.logout())

        val appBar = binding.appBarMainMenu
        val drawerLayout = binding.drawerLayoutHome
        val navView = binding.navView
        val appBarConfig = AppBarConfiguration(
            getHomeNavController().graph,
            drawerLayout
        )

        navView.setupWithNavController(getHomeNavController())
        appBar.setupWithNavController(getHomeNavController(), appBarConfig)

        bindLogoutEvent()
        fillUserData()
    }

    private fun fillUserData(){
        viewModel.getById(auth.currentUser?.uid ?: "").observe(viewLifecycleOwner){ status ->
            when (status) {
                is MainViewModel.Status.Loading -> {}
                is MainViewModel.Status.Failure -> {
                    Log.e("FRAGMENT", status.e.message, status.e)
                }
                is MainViewModel.Status.Success -> {
                    userData = (status.result as MainViewModel.Result.Data<UserData>).obj

                    binding.navView.getHeaderView(0)?.let { header ->
                        val user = auth.currentUser ?: throw Exception("Failed to get current user")

                        header.findViewById<TextView>(R.id.text_nav_header_user_name).text = userData.name
                        header.findViewById<TextView>(R.id.text_nav_header_user_email).text = user.email
                    }
                }
            }
        }
    }

    private fun bindLogoutEvent(){
        val btnLogout = binding.navView.getHeaderView(0)
            .findViewById<ImageButton>(R.id.btn_nav_header_user_logout)

        btnLogout.setOnClickListener {
            auth.signOut()
            getMainNavController().navigate(HomeContainerFragmentDirections.logout())
        }
    }

    private fun getMainNavController() =
        requireActivity().findNavController(R.id.nav_host_main)

    private fun getHomeNavController() =
        (childFragmentManager.findFragmentById(R.id.nav_host_home) as? NavHostFragment)?.navController
            ?: throw Exception("Failed to get nav controller")
}