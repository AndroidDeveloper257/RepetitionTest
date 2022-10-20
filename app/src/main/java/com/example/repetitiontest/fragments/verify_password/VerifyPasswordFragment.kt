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

    private lateinit var binding: FragmentVerifyPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPasswordBinding.inflate(layoutInflater)
        Toast.makeText(requireContext(), "Verify password page", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyPasswordFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}