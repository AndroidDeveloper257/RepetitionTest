package com.example.repetitiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys
import com.example.repetitiontest.const_values.RoomFirebaseStatus
import com.example.repetitiontest.database.AppDatabase
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.ActivityMainBinding
import com.example.repetitiontest.fragments.intro.IntroFragment
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private var user: UserEntity? = null
    private var status: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        takeControl()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.introFragment, R.id.signInFragment, R.id.signUpFragment, R.id.savePersonalDataFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun takeControl() {
        val navGraph = navController.graph

        val extras = intent.extras
        user = extras?.getParcelable(BundleKeys.USER)
        status = extras?.getInt("status")!!
        when (status) {
            // TODO: for remember something about status values look at finishSplash method in SplashAcitivty.kt file
            0 -> {
                navGraph.setStartDestination(R.id.introFragment)
                navController.setGraph(navGraph, null)
            }
            1 -> {
                val bundle = Bundle()
                bundle.putParcelable(BundleKeys.USER, user)
                navGraph.setStartDestination(R.id.signUpFragment)
                navController.setGraph(navGraph, bundle)
            }
            2 -> {
                val bundle = Bundle()
                bundle.putParcelable(BundleKeys.USER, user)
                navGraph.setStartDestination(R.id.homeFragment)
                navController.setGraph(navGraph, bundle)
            }
            else -> {}
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}