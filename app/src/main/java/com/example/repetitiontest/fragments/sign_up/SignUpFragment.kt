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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()

        binding.eye.setOnClickListener {
            if (isPasswordVisible) {
                /**
                 * make invisible
                 */
                binding.eye.setImageResource(R.drawable.password_invisible)
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                /**
                 * make visible
                 */
                binding.eye.setImageResource(R.drawable.password_visible)
                binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            isPasswordVisible = !isPasswordVisible
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }
}