package com.example.repetitiontest.fragments.intro

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.repetitiontest.R
import com.example.repetitiontest.adapters.IntroPageAdapter
import com.example.repetitiontest.const_values.FirebaseKeys.USERS
import com.example.repetitiontest.database.UserEntity
import com.example.repetitiontest.databinding.FragmentIntroBinding
import com.google.firebase.database.*

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: IntroPageAdapter
    private lateinit var pageList: ArrayList<String>

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var firebaseUsers: ArrayList<UserEntity>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(layoutInflater)
        generatePages()
        adapter = IntroPageAdapter(
            this,
            pageList
        )
        binding.viewPager.adapter = adapter
        binding.indicator.attachTo(binding.viewPager)

        binding.continueBtn.setOnClickListener {
            if (binding.viewPager.currentItem == pageList.size - 1) {
                openNextPage()
            } else {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (binding.viewPager.currentItem == pageList.size - 1) {
                    binding.continueBtn.text = getString(R.string.end_intro)
                } else {
                    binding.continueBtn.text = getString(R.string.continue_intro)
                }
            }
        })

        return binding.root
    }

    private fun loadFromFireBase() {
        firebaseUsers = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference(USERS)
        reference
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        /**
                         * users exist
                         */
                        Log.d(TAG, "onDataChange: users exist")
                        snapshot.children.forEach {
                            val value = it.getValue(UserEntity::class.java)
                            if (value != null) {
                                firebaseUsers.add(value)
                            }
                        }
                        Log.d(TAG, "onDataChange: ${firebaseUsers.size} users found")
                    } else {
                        /**
                         * firebase realtime database is empty
                         */
                        Log.d(TAG, "onDataChange: firebase realtime database is empty")
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

    private fun checkDatabase(): Boolean {
        /**
         * returns true for sign in page if database is empty
         * returns false for main page if database is not empty
         */
//        database = AppDatabase.getInstance(requireContext())
//        databaseUsers = ArrayList(database.userDao().getDatabaseUsers())
//        return if (databaseUsers.isEmpty()) {
            /**
             * database is empty
             * sign in or sign up
             */
//            Log.d(TAG, "checkDatabase: database is empty sign in or sign up")
//            true
//        } else {
            /**
             * main pagega o'tadi
             */
//            false
//        }
        return true
        // TODO: uncomment above after got ready entity classap
    }

    private fun openNextPage() {
//        if (checkDatabase()) {
//            findNavController().navigate(R.id.signInFragment)
//        } else {
//            /**
//             * check firebase
//             * if phone number is still in firebase -> main page
//             * else -> sign up
//             */
//        }
        findNavController().navigate(R.id.signInFragment)
    }

    private fun generatePages() {
        pageList = ArrayList()
        pageList.add("Bu yerda introdagi gaplar bo'lishi kerak edi")
        pageList.add("Bu yerda introdagi gaplar bo'lishi kerak edi")
        pageList.add("Bu yerda introdagi gaplar bo'lishi kerak edi")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "IntroFragment"
    }
}