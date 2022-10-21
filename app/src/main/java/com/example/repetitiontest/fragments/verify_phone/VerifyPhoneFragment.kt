package com.example.repetitiontest.fragments.verify_phone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.repetitiontest.databinding.FragmentVerifyPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class VerifyPhoneFragment : Fragment() {

    private var binding: FragmentVerifyPhoneBinding? = null
    private var phoneNumber: String? = null
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(layoutInflater)
        val s = arguments?.getString("phone_number")
        if (s != null) {
            phoneNumber = s
        }

        sendCode()

        binding?.codeEt?.addTextChangedListener {
            val code = it.toString()
            Log.d(TAG, "onCreateView: $code")
            if (code.length == 6) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId.toString(), code)
                signInWithPhoneAuthCredential(credential)
            }
        }

        return binding?.root
    }

    private fun sendCode() {
        auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: $credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e(TAG, "onVerificationFailed: ${e.message}")
            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            Log.d(TAG, "onCodeSent: $verificationId")

            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithPhoneAuthCredential: succcess")
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "signInWithPhoneAuthCredential: ${task.exception?.message}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Wrong code", Toast.LENGTH_SHORT).show()
                    }
                    binding?.codeEt?.setText("")
                }
            }
    }

    companion object {
        private const val TAG = "VerifyPhoneFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}