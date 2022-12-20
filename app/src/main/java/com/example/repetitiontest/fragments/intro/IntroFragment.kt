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
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys.USERS
import com.example.repetitiontest.const_values.RoomFirebaseStatus
import com.example.repetitiontest.database.AppDatabase
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.FragmentIntroBinding
import com.example.repetitiontest.helper_functions.showToast
import com.google.firebase.database.*

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: IntroPageAdapter
    private lateinit var pageList: ArrayList<String>

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var database: AppDatabase

    private var databaseUser: UserEntity? = null

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

        openNextPage()

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
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference(USERS)
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
                        findNavController().navigate(R.id.homeFragment, bundle)
                    } else {
                        /**
                         * room ✅
                         * firebase ❌
                         * sign up for this number
                         */
                        bundle.putInt(
                            BundleKeys.ROOM_FIREBASE_STATUS,
                            RoomFirebaseStatus.ROOM_OK_FIREBASE_NO
                        )
                        findNavController().navigate(R.id.signUpFragment, bundle)
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

    private fun isDatabaseEmpty(): Boolean {
        /**
         * returns true for sign in page if database is empty
         * returns false for main page if database is not empty
         */
        database = AppDatabase.getInstance(requireContext())
        val databaseUsers = ArrayList(database.userDao().getDatabaseUsers())
        try {
            databaseUser = databaseUsers[0]
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "isDatabaseEmpty: database is empty")
        }
        return if (databaseUsers.isEmpty()) {
            /**
             * database is empty
             * open sign in page
             */
            Log.d(TAG, "checkDatabase: database is empty, now sign in page will open")
            true
        } else {
            /**
             * database is not empty
             * check firebase
             * roomdagi userni firebasedayam bor yo'qligini tekshiramiz
             * bor bo'sa main page
             * aks holda sign up for this number
             */
            false
        }
    }

    private fun openNextPage() {
        if (isDatabaseEmpty()) {
            if (binding.viewPager.currentItem == pageList.size - 1) {
                /**
                 * room ❌
                 */
                findNavController().navigate(R.id.signInFragment)
            }
        } else {
            showToast(requireContext(), "database is not empty")
            /**
             * room ✅
             */
            loadFromFireBase()
        }
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