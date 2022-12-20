package com.example.repetitiontest.fragments.main_page.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.database.AppDatabase
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.FragmentHomeBinding
import com.example.repetitiontest.helper_functions.showToast
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userEntity: UserEntity
    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        if (arguments?.getParcelable<UserEntity>(BundleKeys.USER) != null) {
            userEntity = arguments?.getParcelable(BundleKeys.USER)!!
        } else {

        }
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitProcess(1)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        showToast(requireContext(), "Welcome ${userEntity.fullName}")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}