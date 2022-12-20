package com.example.repetitiontest.fragments.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.database.UserEntity
import com.example.repetitiontest.databinding.FragmentSavePersonalDataBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SavePersonalDataFragment : Fragment() {

    private var _binding: FragmentSavePersonalDataBinding? = null
    private val binding get() = _binding!!

    private var user: UserEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavePersonalDataBinding.inflate(layoutInflater)
        user = arguments?.getParcelable(BundleKeys.USER)

        binding.emailEt.setOnClickListener {
            signInWithGoogle()
        }

        return binding.root
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(requireContext(), googleSignInOptions)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SavePersonalDataFragmen"
    }
}