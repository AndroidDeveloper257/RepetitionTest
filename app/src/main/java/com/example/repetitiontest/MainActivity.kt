package com.example.repetitiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.repetitiontest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        navController = findNavController(R.id.fragmentContainerView)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.introFragment, R.id.signInFragment, R.id.signUpFragment, R.id.savePersonalDataFragment -> {
//                    binding.bottomNavigationView.visibility = View.GONE
//                }
//                else -> {
//                    binding.bottomNavigationView.visibility = View.VISIBLE
//                }
//            }
//        }
    }
}