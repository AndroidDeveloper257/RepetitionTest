package com.example.repetitiontest.fragments.enter_phone_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentEnterPhoneNumberBinding

class EnterPhoneNumberFragment : Fragment() {

    private var binding: FragmentEnterPhoneNumberBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterPhoneNumberBinding.inflate(layoutInflater)
        Toast.makeText(requireContext(), "Enter phone number page", Toast.LENGTH_SHORT).show()
        return binding?.root
    }

    companion object {
        private const val TAG = "EnterPhoneNumberFragmen"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterPhoneNumberFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}