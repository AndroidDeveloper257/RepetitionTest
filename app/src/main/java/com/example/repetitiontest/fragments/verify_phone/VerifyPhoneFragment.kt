package com.example.repetitiontest.fragments.verify_phone

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
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
    private var handler = Handler()
    private var time = 60

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(layoutInflater)
        val s = arguments?.getString(BundleKeys.PHONE_NUMBER)
        if (s != null) {
            phoneNumber = s
        }

        sendCode()

        binding?.codeEt?.addTextChangedListener {
            val code = it.toString()
            Log.d(TAG, "onCreateView: $code")
            if (code.length == 6) {
                val credential =
                    PhoneAuthProvider.getCredential(storedVerificationId.toString(), code)
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
        binding?.progress?.visibility = View.VISIBLE
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (time == 0) {
                Toast.makeText(requireContext(), "time is up", Toast.LENGTH_SHORT).show()
            } else time--
            updateTimer()
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(this, 1000)
        }

    }

    private fun updateTimer() {
        binding?.timerTv?.text = time.toString()
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
            binding?.progress?.visibility = View.GONE
            Toast.makeText(
                requireContext(),
                "Verification code send to $phoneNumber",
                Toast.LENGTH_SHORT
            ).show()
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
                    openNextPage()
                } else {
                    Log.e(TAG, "signInWithPhoneAuthCredential: ${task.exception?.message}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Wrong code", Toast.LENGTH_SHORT).show()
                    }
                    binding?.codeEt?.setText("")
                }
            }
    }

    private fun openNextPage() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        val bundle = Bundle()
        bundle.putString(BundleKeys.PHONE_NUMBER, phoneNumber)
        findNavController().navigate(R.id.enterPasswordFragment, bundle, navOptions)
    }

    companion object {
        private const val TAG = "VerifyPhoneFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}