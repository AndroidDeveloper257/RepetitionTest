package com.example.repetitiontest.fragments.registration.verify_phone

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
import com.example.repetitiontest.const_values.BundleKeys.PHONE_NUMBER
import com.example.repetitiontest.databinding.FragmentVerifyPhoneBinding
import com.example.repetitiontest.fragments.registration.enter_phone_number.EnterPhoneNumberFragment
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyPhoneFragment : Fragment() {

    private var _binding: FragmentVerifyPhoneBinding? = null
    private val binding get() = _binding!!

    private var phoneNumber: String? = null
    private var timer = 120
    private var handler = Handler()

    private lateinit var auth: FirebaseAuth
    private var storedFVerificationId: String? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyPhoneBinding.inflate(layoutInflater)

        if (arguments?.getString(PHONE_NUMBER) != null) {
            phoneNumber = arguments?.getString(PHONE_NUMBER)
        }

//        sendVerificationCode()
        binding.codeSentTv.text = "Verification code sent to $phoneNumber"

        binding.verificationCodeEt.addTextChangedListener {
            binding.submitCodeBtn.isEnabled = it.toString().length == 6
        }

        binding.submitCodeBtn.setOnClickListener {
            // TODO: push qilishdan oldin bir ko'r
            /**
             * Hozircha bu joyi commentda turibdi
             * Testing jarayoni tugaganda 73-qator o'chiriladi commentlar ochiladi
             */
            /*val verificationCode = binding.verificationCodeEt.text.toString()
                val credential =
                    PhoneAuthProvider.getCredential(storedFVerificationId ?: "", verificationCode)
                signInWithPhoneAuthCredentials(credential)*/
            codeCorrect()
        }

        return binding.root
    }

    private fun signInWithPhoneAuthCredentials(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Successfully completed", Toast.LENGTH_SHORT)
                        .show()
                    codeCorrect()
                } else {
                    Log.e(TAG, "signInWithPhoneAuthCredentials: failed ${task.exception}")
                    Log.e(TAG, "signInWithPhoneAuthCredentials: failed ${task.exception?.message}")
                    Toast.makeText(requireContext(), "UnSuccessfully completed", Toast.LENGTH_SHORT)
                        .show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Wrong code", Toast.LENGTH_SHORT).show()
                        binding.verificationCodeEt.setText("")
                    }
                }
            }
    }

    private fun codeCorrect() {
        Toast.makeText(requireContext(), "Correct", Toast.LENGTH_SHORT).show()
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        val bundle = Bundle()
        bundle.putString(PHONE_NUMBER, phoneNumber)
        if (isNumberRegistered()) {
            findNavController().navigate(R.id.verifyPasswordFragment, bundle, navOptions)
        } else {
            findNavController().navigate(R.id.setPasswordFragment, bundle, navOptions)
        }
    }

    private fun isNumberRegistered(): Boolean {
//        return true
        return false
    }

    private fun sendVerificationCode() {
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
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: $p0")
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.e(TAG, "onVerificationFailed: ${p0.message}")
            Log.e(TAG, "onVerificationFailed: $p0")
            if (p0 is FirebaseAuthInvalidCredentialsException) {
                Log.e(TAG, "onVerificationFailed: Invalid request")
                Toast.makeText(requireContext(), "Invalid request", Toast.LENGTH_SHORT).show()
            } else if (p0 is FirebaseTooManyRequestsException) {
                Log.e(TAG, "onVerificationFailed: Too many requests")
                Log.e(TAG, "onVerificationFailed: $p0")
                Toast.makeText(requireContext(), "Too many requests", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "$p0", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            binding.codeSentTv.text = "Verification code sent to $phoneNumber"
            Toast.makeText(
                requireContext(),
                binding.codeSentTv.text.toString(),
                Toast.LENGTH_SHORT
            ).show()
            runnable.run()
            storedFVerificationId = p0
            resendingToken = p1
        }

    }

    private val runnable = object : Runnable {
        override fun run() {
            if (timer == 0) {
                timeOut()
            } else {
                updateTimer()
            }
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(this, 1000)
        }

    }

    private fun updateTimer() {
        val minutes = timer / 60
        val seconds = timer - minutes * 60
        binding.timerTv.text =
            "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        timer--
    }

    private fun timeOut() {

    }

    companion object {
        private const val TAG = "VerifyPhoneFragment"

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