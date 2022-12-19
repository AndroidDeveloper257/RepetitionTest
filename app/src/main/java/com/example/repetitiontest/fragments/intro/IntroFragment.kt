package com.example.repetitiontest.fragments.intro

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.repetitiontest.R
import com.example.repetitiontest.adapters.IntroPageAdapter
import com.example.repetitiontest.databinding.FragmentIntroBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: IntroPageAdapter
    private lateinit var pageList: ArrayList<String>

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

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

    private fun openNextPage() {

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