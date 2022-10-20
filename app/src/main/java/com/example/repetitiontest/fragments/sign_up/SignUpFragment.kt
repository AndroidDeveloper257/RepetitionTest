package com.example.repetitiontest.fragments.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
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

            }
        }
        return binding.root
    }

    private fun isValid(phoneNumber: String): Boolean {
        // TODO: check phone number is true
        return true
    }

    companion object {
        private const val TAG = "SignUpFragment"
        private lateinit var pageList: ArrayList<String>
    }
}