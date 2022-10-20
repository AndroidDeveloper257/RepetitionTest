package com.example.repetitiontest.fragments.verify_phone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentVerifyPhoneBinding

class VerifyPhoneFragment : Fragment() {

    private var binding: FragmentVerifyPhoneBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(layoutInflater)

        return binding?.root
    }

    companion object {
        private const val TAG = "VerifyPhoneFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}