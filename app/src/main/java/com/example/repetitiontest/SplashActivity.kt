package com.example.repetitiontest

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys
import com.example.repetitiontest.database.AppDatabase
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.ActivitySplashBinding
import com.google.firebase.database.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var appDatabase: AppDatabase
    private var databaseUser: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkDatabase()
    }

    private fun checkDatabase() {
        appDatabase = AppDatabase.getInstance(this)
        val users = ArrayList(appDatabase.userDao().getDatabaseUsers())
        if (users.isNotEmpty()) {
            databaseUser = users[0]
            loadFromFireBase()
        } else {
            finishSplash(0)
        }
    }

    private fun finishSplash(status: Int) {
        /**
         * status = 0   ->   room is empty  ->  open sign in page, then or sign up or main page
         * status = 1   ->   room is not empty && firebase is empty -> sign up for this number
         * status = 2   ->   room is not empty && firebase is not empty -> main page
         */
        val bundle = Bundle()
        bundle.putParcelable(BundleKeys.USER, databaseUser)
        bundle.putInt("status", status)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finishAffinity()
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

                    if (found) {
                        /**
                         * main page
                         */
                        finishSplash(2)
                    } else {
                        /**
                         * room ✅
                         * firebase ❌
                         * sign up for this number
                         */
                        finishSplash(1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: something went wrong")
                    Log.e(TAG, "onCancelled: ${error.message}")
                    Log.e(TAG, "onCancelled: ${error.toException().message}")
                    Log.e(TAG, "onCancelled: ${error.toException().printStackTrace()}")
                }

            })
    }

    companion object {
        private const val TAG = "SplashActivity"
    }
}