package com.example.repetitiontest.fragments.sign_in

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys
import com.example.repetitiontest.database.UserEntity
import com.example.repetitiontest.databinding.CustomLoadingDialogBinding
import com.example.repetitiontest.databinding.FragmentSignInBinding
import com.google.firebase.database.*

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private var phoneNumber: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var state = 1

    private var userEntity: UserEntity? = null

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
            }
        }

        return binding.root
    }

    private fun useFirebase() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        val dialogBinding = CustomLoadingDialogBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        var found = false
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
                                Log.d(TAG,"onDataChange: this number has been registered on firebase")
                                userEntity = value
                                alertDialog.dismiss()
                                state = 2
                            }
                        }
                    }
                } else {
                    /**
                     * firebase is empty for now
                     * should sign up
                     */
                    Log.d(TAG, "onDataChange: firebase is empty for now, user should sign up")
                    val bundle = Bundle()
                    bundle.putString(BundleKeys.PHONE_NUMBER, phoneNumber)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}