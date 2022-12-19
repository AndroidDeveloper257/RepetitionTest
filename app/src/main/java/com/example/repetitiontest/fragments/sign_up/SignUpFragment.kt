package com.example.repetitiontest.fragments.sign_up

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private var isPasswordVisible = true
    private var isCodeConfirmed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()

        binding.confirmBtn.setOnClickListener {

        }

        binding.passwordEye.setOnClickListener {
            if (isPasswordVisible) {
                /**
                 * make invisible
                 */
                binding.passwordEye.setImageResource(R.drawable.password_invisible)
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordEt.setSelection(binding.passwordEt.text.toString().length)
            } else {
                /**
                 * make visible
                 */
                binding.passwordEye.setImageResource(R.drawable.password_visible)
                binding.passwordEt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            isPasswordVisible = !isPasswordVisible
            binding.passwordEt.setSelection(binding.passwordEt.text.toString().length)
        }

        return binding.root
    }

    private fun codeVerificationMode() {
        binding.image1.setImageResource(R.drawable.sms_code_icon)
        binding.verificationCodeEt.visibility = View.VISIBLE

        binding.passwordEt.visibility = View.GONE
        binding.passwordEye.visibility = View.GONE
        binding.imageLayout2.visibility = View.GONE
        binding.confirmPasswordEt.visibility = View.GONE
        binding.confirmPasswordEye.visibility = View.GONE
    }

    private fun setPasswordMode() {
        binding.image1.setImageResource(R.drawable.password_icon)
        binding.verificationCodeEt.visibility = View.INVISIBLE

        binding.passwordEt.visibility = View.VISIBLE
        binding.passwordEye.visibility = View.VISIBLE
        binding.imageLayout2.visibility = View.VISIBLE
        binding.confirmPasswordEt.visibility = View.VISIBLE
        binding.confirmPasswordEye.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }
}