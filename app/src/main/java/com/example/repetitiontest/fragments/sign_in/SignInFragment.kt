package com.example.repetitiontest.fragments.sign_in

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys
import com.example.repetitiontest.database.UserEntity
import com.example.repetitiontest.databinding.CustomLoadingDialogBinding
import com.example.repetitiontest.databinding.FragmentSignInBinding
import com.example.repetitiontest.helper_functions.phoneNumberToId
import com.google.firebase.database.*

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private var phoneNumber: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var state = 1

    private var userEntity: UserEntity? = null
    private var isPasswordVisible = true

    /**
     * state = 1 if phone number entering
     * state = 2 if signing in to account
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater)

        binding.signInBtn.setOnClickListener {
            if (state == 1) {
                useFirebase()
            } else {
                checkPassword()
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

        return binding.root
    }

    private fun checkPassword() {
        if (userEntity?.password == binding.passwordEt.text.toString()) {
            // TODO: sign in page is still not ready
        }
    }

    private fun useFirebase() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        val dialogBinding = CustomLoadingDialogBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference(FirebaseKeys.USERS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val value = it.getValue(UserEntity::class.java)
                        if (value != null) {
                            if (value.phoneNumber == phoneNumber) {
                                /**
                                 * this number has been registered on firebase
                                 * should sign in
                                 */
                                Log.d(
                                    TAG,
                                    "onDataChange: this number has been registered on firebase"
                                )
                                userEntity = value
                                alertDialog.dismiss()
                                state = 2
                                stateTwoMode()
                            }
                        }
                    }
                } else {
                    alertDialog.dismiss()
                    /**
                     * firebase is empty for now
                     * should sign up
                     */
                    Log.d(TAG, "onDataChange: firebase is empty for now, user should sign up")
                    phoneNumber = binding.phoneNumberEt.text.toString()
                    val bundle = Bundle()
                    val user = UserEntity(
                        phoneNumberToId(phoneNumber),
                        phoneNumber = phoneNumber
                    )
                    bundle.putParcelable(BundleKeys.USER, user)
                    val navOptions: NavOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.enter)
                        .setExitAnim(R.anim.exit)
                        .setPopEnterAnim(R.anim.pop_enter)
                        .setPopExitAnim(R.anim.pop_exit)
                        .build()
                    findNavController().navigate(R.id.signUpFragment, bundle, navOptions)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: something went wrong")
                Log.e(TAG, "onCancelled: ${error.message}")
                Log.e(TAG, "onCancelled: ${error.toException().message}")
                Log.e(TAG, "onCancelled: ${error.toException().printStackTrace()}")
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun stateTwoMode() {
        binding.passwordEt.visibility = View.VISIBLE
        binding.passwordEye.visibility = View.VISIBLE
        binding.imageLayout2.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}