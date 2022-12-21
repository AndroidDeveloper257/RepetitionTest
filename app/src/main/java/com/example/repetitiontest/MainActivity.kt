package com.example.repetitiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var appDatabase: AppDatabase

    companion object {
        private const val TAG = "MainActivity"
    }

    private var databaseUser: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        checkDatabase()
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

    // TODO: main page is still empty

    private fun checkDatabase() {
        appDatabase = AppDatabase.getInstance(this)
        val users = ArrayList(appDatabase.userDao().getDatabaseUsers())
        if (users.isNotEmpty()) {
            databaseUser = users[0]
            loadFromFireBase()
        }
    }

    private fun loadFromFireBase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference(FirebaseKeys.USERS)
        reference
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var found = false
                    if (snapshot.exists()) {
                        /**
                         * users exist
                         */
                        Log.d(TAG, "onDataChange: users exist")
                        snapshot.children.forEach {
                            val value = it.getValue(UserEntity::class.java)
                            if (value != null) {
                                if (value.phoneNumber == databaseUser?.phoneNumber) {
                                    /**
                                     * room ✅
                                     * firebase ✅
                                     * open main page
                                     */

                                    found = true
                                    Log.d(
                                        TAG,
                                        "onDataChange: $databaseUser found on firebase realtime database"
                                    )
                                }
                            }
                        }
                    } else {
                        /**
                         * room ✅
                         * firebase ❌
                         * open sign up page
                         */
                        Log.d(TAG, "onDataChange: firebase realtime database is empty")
                    }

                    val bundle = Bundle()
                    bundle.putParcelable(BundleKeys.USER, databaseUser)
                    if (found) {
                        /**
                         * main page
                         */
                        navController.navigate(R.id.homeFragment, bundle)
                    } /*else {
                        *//**
                         * room ✅
                         * firebase ❌
                         * sign up for this number
                         *//*
                        bundle.putInt(
                            BundleKeys.ROOM_FIREBASE_STATUS,
                            RoomFirebaseStatus.ROOM_OK_FIREBASE_NO
                        )
                        navController.navigate(R.id.signUpFragment, bundle)
                    }*/
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: something went wrong")
                    Log.e(TAG, "onCancelled: ${error.message}")
                    Log.e(TAG, "onCancelled: ${error.toException().message}")
                    Log.e(TAG, "onCancelled: ${error.toException().printStackTrace()}")
                }

            })
    }
}