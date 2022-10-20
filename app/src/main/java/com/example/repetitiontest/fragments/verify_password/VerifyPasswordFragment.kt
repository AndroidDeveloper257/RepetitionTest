package com.example.repetitiontest.fragments.verify_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentVerifyPasswordBinding

class VerifyPasswordFragment : Fragment() {

    private var binding: FragmentVerifyPasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPasswordBinding.inflate(layoutInflater)
        return binding?.root
    }

    companion object {
        private const val TAG = "VerifyPasswordFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}