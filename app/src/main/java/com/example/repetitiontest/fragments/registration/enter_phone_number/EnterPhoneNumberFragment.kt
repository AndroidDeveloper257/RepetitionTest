package com.example.repetitiontest.fragments.registration.enter_phone_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentEnterPhoneNumberBinding

class EnterPhoneNumberFragment : Fragment() {

    private var _binding: FragmentEnterPhoneNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterPhoneNumberBinding.inflate(layoutInflater)
        binding.submitNumber.setOnClickListener {
            val phoneNumber = binding.phoneNumberEt.text.toString()
            if (isValid(phoneNumber)) {
                val navOptions: NavOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.enter)
                    .setExitAnim(R.anim.exit)
                    .setPopEnterAnim(R.anim.pop_enter)
                    .setPopExitAnim(R.anim.pop_exit)
                    .build()
                val bundle = Bundle()
                bundle.putString("phone_number", phoneNumber)
                findNavController().navigate(R.id.verifyPhoneFragment, bundle, navOptions)
            } else {
                // TODO: phone number is wrong
                Toast.makeText(requireContext(), "Phone number is wrong", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun isValid(phoneNumber: String): Boolean {
        // TODO: check phone number is true
        return true
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
        _binding = null
    }
}