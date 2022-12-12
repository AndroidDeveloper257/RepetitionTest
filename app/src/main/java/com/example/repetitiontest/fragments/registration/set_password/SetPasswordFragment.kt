package com.example.repetitiontest.fragments.registration.set_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys.PASSWORD
import com.example.repetitiontest.const_values.BundleKeys.PHONE_NUMBER
import com.example.repetitiontest.databinding.FragmentSetPasswordBinding

class SetPasswordFragment : Fragment() {

    private var _binding: FragmentSetPasswordBinding? = null
    private val binding get() = _binding!!

    private var phoneNumber: String? = null
    private lateinit var password: String
    private lateinit var confirmPassword: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetPasswordBinding.inflate(layoutInflater)

        if (arguments?.getString(PHONE_NUMBER) != null) {
            phoneNumber = arguments?.getString(PHONE_NUMBER)
        }

        binding.confirmPassword.editText?.addTextChangedListener {
            confirmPassword = it.toString()
            binding.saveBtn.isEnabled = confirmPassword == binding.password.editText?.text.toString()
        }

        binding.saveBtn.setOnClickListener {
            password = binding.password.editText?.text.toString()
            val navOptions: NavOptions = NavOptions.Builder()
                                    .setEnterAnim(R.anim.enter)
                                    .setExitAnim(R.anim.exit)
                                    .setPopEnterAnim(R.anim.pop_enter)
                                    .setPopExitAnim(R.anim.pop_exit)
                                    .build()
            val bundle = Bundle()
            bundle.putString(PHONE_NUMBER, phoneNumber)
            bundle.putString(PASSWORD, password)
            if (isPasswordStrong()) {
                findNavController().navigate(R.id.setPersonalDataFragment, bundle, navOptions)
            } else {
                Toast.makeText(requireContext(), "Your password is weak", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun isPasswordStrong(): Boolean {
        return true
    }

    companion object {
        private const val TAG = "SetPasswordFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetPasswordFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}