package com.example.repetitiontest.fragments.enter_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.databinding.FragmentEnterPasswordBinding

class EnterPasswordFragment : Fragment() {

    private var binding: FragmentEnterPasswordBinding? = null
    private lateinit var password: String
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterPasswordBinding.inflate(layoutInflater)
        val s = arguments?.getString(BundleKeys.PHONE_NUMBER)
        if (s != null) {
            phoneNumber = s
        }

        binding?.saveBtn?.setOnClickListener {
            saveClicked()
        }

        return binding?.root
    }

    private fun saveClicked() {
        if (!isPasswordConfirmed()) {
            binding?.password?.isErrorEnabled = false
            binding?.confirmPassword?.isErrorEnabled = true
            binding?.confirmPassword?.error = "Please confirm password"
            return
        } else {
            binding?.confirmPassword?.isErrorEnabled = false
        }

        if (!isPasswordStrong()) {
            binding?.password?.isErrorEnabled = true
            binding?.password?.error = requireContext().getString(R.string.strong_password_requirements)
            return
        } else {
            binding?.password?.isErrorEnabled = false
        }

        openNextPage()
    }

    private fun openNextPage() {
        val bundle = Bundle()
        bundle.putString(BundleKeys.PHONE_NUMBER, phoneNumber)
        bundle.putString(BundleKeys.PASSWORD, password)
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        findNavController().navigate(R.id.enterPersonalDataFragment, bundle, navOptions)
    }

    private fun isPasswordStrong(): Boolean {
        password = binding?.password?.editText?.text.toString()
        if (password.length < 8) {
            return false
        }
        var upperCase = false
        var lowerCase = false
        var numbers = false
        for (c in password) {
            if (Character.isUpperCase(c)) upperCase = true
            if (Character.isLowerCase(c)) lowerCase = true
            if (c.toInt() in 48..57) numbers = true
        }
        return numbers && upperCase && lowerCase
    }

    private fun isPasswordConfirmed(): Boolean {
        return binding?.confirmPassword?.editText?.text.toString() == binding?.password?.editText?.text.toString()
    }

    companion object {
        private const val TAG = "EnterPasswordFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}