package com.example.repetitiontest.fragments.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.IntroKeys.PAGE_TITLE
import com.example.repetitiontest.databinding.FragmentIntroPageBinding

class IntroPageFragment : Fragment() {

    private var _binding: FragmentIntroPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroPageBinding.inflate(layoutInflater)
        val pageTitle = arguments?.getString(PAGE_TITLE)
        binding.tv.text = pageTitle
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "IntroPageFragment"
        @JvmStatic
        fun newInstance(pageText: String) =
            IntroPageFragment().apply {
                arguments = Bundle().apply {
                    putString(PAGE_TITLE, pageText)
                }
            }
    }
}