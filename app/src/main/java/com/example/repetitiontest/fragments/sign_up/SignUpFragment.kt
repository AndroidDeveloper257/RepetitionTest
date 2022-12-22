package com.example.repetitiontest.fragments.sign_up

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.FragmentSignUpBinding
import com.example.repetitiontest.helper_functions.makeupPhoneNumber
import com.example.repetitiontest.helper_functions.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private var isPasswordVisible = true
    private var isConfirmPasswordVisible = true
    private var user: UserEntity? = null

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private var isPhoneVerified = false

    private var time = 120
    private var handler = Handler()

    private var code: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        binding.passwordEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
        binding.confirmPasswordEt.transformationMethod =
            HideReturnsTransformationMethod.getInstance()
        user = arguments?.getParcelable(BundleKeys.USER)

        sendVerificationCode()
        val makeupPhoneNumber = makeupPhoneNumber(user?.phoneNumber)

        binding.codeSentTv.text =
            "${getString(R.string.verification_code_sent_1)} $makeupPhoneNumber ${getString(R.string.verification_code_sent_2)}"

        binding.confirmBtn.setOnClickListener {
            if (!isPhoneVerified) {
                /**
                 * check code
                 */
                checkCode()
            } else {
                /**
                 * check password
                 */
                checkPasswords()
            }
        }

        binding.passwordEye.setOnClickListener {
            if (isPasswordVisible) {
                /**
                 * make invisible
                 */
                binding.passwordEye.setImageResource(R.drawable.password_invisible)
                binding.passwordEt.transformationMethod = PasswordTransformationMethod.getInstance()
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

        binding.confirmPasswordEye.setOnClickListener {
            if (isConfirmPasswordVisible) {
                /**
                 * make invisible
                 */
                binding.confirmPasswordEye.setImageResource(R.drawable.password_invisible)
                binding.confirmPasswordEt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            } else {
                /**
                 * make visible
                 */
                binding.confirmPasswordEye.setImageResource(R.drawable.password_visible)
                binding.confirmPasswordEt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            binding.confirmPasswordEt.setSelection(binding.passwordEt.text.toString().length)
        }

        binding.resendCode.setOnClickListener {
            time = 120
            resendVerificationCode()
        }

        return binding.root
    }

    private fun checkPasswords() {
        if (binding.confirmPasswordEt.text.toString() != binding.passwordEt.text.toString()) {
            showToast(requireContext(), "Please write correct confirm password")
        } else {
            if (isPasswordStrong()) {
                openNextPage()
            } else {
                showToast(requireContext(), "You password is weak")
            }
        }
    }

    private fun openNextPage() {
        user?.password = binding.passwordEt.text.toString()
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        val bundle = Bundle()
        bundle.putParcelable(BundleKeys.USER, user)
        findNavController().navigate(R.id.savePersonalDataFragment, bundle, navOptions)
    }

    private fun isPasswordStrong(): Boolean {
        return true
    }

    private fun checkCode() {
        code = binding.verificationCodeEt.text.toString()
        if (code!!.length == 6) {
            val credential = PhoneAuthProvider.getCredential(
                storedVerificationId ?: "", code.toString()
            )
            signInWithCredential(credential)
        } else {
            showToast(requireContext(), "Kod 6 xonali son bo'lishi kerak")
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential: success")
                    isPhoneVerified = true
                    setPasswordMode()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showToast(requireContext(), "Noto'g'ri kod kiritildi")
                    }
                }
            }
    }

    private fun sendVerificationCode() {
        auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions
            .Builder(auth)
            .setPhoneNumber(user?.phoneNumber.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendVerificationCode() {
        auth = FirebaseAuth.getInstance()
        val options = resendingToken?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(user?.phoneNumber.toString())       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .setForceResendingToken(it)
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: $credential")
        }

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            Log.e(TAG, "onVerificationFailed: ${firebaseException.message}")
            Log.e(TAG, "onVerificationFailed: ${firebaseException.printStackTrace()}")
            showToast(requireContext(), "Nimadir xato bo'ldi")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendingToken = token
            Log.d(TAG, "onCodeSent: verificationId -> $verificationId")
            Log.d(TAG, "onCodeSent: token -> $token")
            showToast(requireContext(), "Kod jo'natildi")
            binding.resendCode.visibility = View.GONE
            runnable.run()
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (time <= 0) {
                binding.resendCode.visibility = View.VISIBLE
            }
            updateTimer()
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(this, 1000)
        }
    }

    private fun updateTimer() {
        val minute = time / 60
        val second = time - minute * 60
        binding.timerTv.text =
            "${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}"
        if (time > 0) {
            time--
        }
    }

//    private fun codeVerificationMode() {
//        binding.image1.setImageResource(R.drawable.sms_code_icon)
//        binding.verificationCodeEt.visibility = View.VISIBLE

//        binding.passwordEt.visibility = View.GONE
//        binding.passwordEye.visibility = View.GONE
//        binding.imageLayout2.visibility = View.GONE
//        binding.confirmPasswordEt.visibility = View.GONE
//        binding.confirmPasswordEye.visibility = View.GONE
//    }

    private fun setPasswordMode() {
        binding.image1.setImageResource(R.drawable.password_icon)
        binding.verificationCodeEt.visibility = View.INVISIBLE
        binding.codeSentTv.visibility = View.GONE
        binding.timerTv.visibility = View.GONE
        binding.resendCode.visibility = View.GONE

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