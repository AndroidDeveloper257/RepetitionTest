package com.example.repetitiontest.fragments.registration.verify_password

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentVerifyPasswordBinding

class VerifyPasswordFragment : Fragment() {

    private var _binding: FragmentVerifyPasswordBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyPasswordBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        private const val TAG = "VerifyPasswordFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}