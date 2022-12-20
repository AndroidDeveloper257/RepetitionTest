package com.example.repetitiontest.fragments.sign_up

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.repetitiontest.R
import com.example.repetitiontest.adapters.UserRoleAdapter
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.const_values.FirebaseKeys
import com.example.repetitiontest.database.AppDatabase
import com.example.repetitiontest.database.users.UserEntity
import com.example.repetitiontest.databinding.CustomLoadingDialogBinding
import com.example.repetitiontest.databinding.FragmentSavePersonalDataBinding
import com.example.repetitiontest.helper_functions.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SavePersonalDataFragment : Fragment() {

    private var _binding: FragmentSavePersonalDataBinding? = null
    private val binding get() = _binding!!

    private var user: UserEntity? = null
    private lateinit var roleList: ArrayList<String>
    private lateinit var unFilledFields: ArrayList<String>

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavePersonalDataBinding.inflate(layoutInflater)
        user = arguments?.getParcelable(BundleKeys.USER)

        binding.emailLayout.setOnClickListener {
            signInWithGoogle()
        }

        setSpinner()
        binding.prevBtn.setOnClickListener {
            if (fieldsCorrect()) {
                createAccount()
            }
        }

        return binding.root
    }

    private fun createAccount() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        val dialogBinding = CustomLoadingDialogBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        dialogBinding.loadingTv.text = getString(R.string.please_wait_account_is_creating)
        alertDialog.show()

        user?.fullName = binding.fullNameEt.text.toString()
        user?.email = binding.emailEt.hint.toString()
        user?.userRole = binding.roleSpinner.selectedItemPosition
        appDatabase = AppDatabase.getInstance(requireContext())
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference(FirebaseKeys.USERS)
        reference
            .child(user?.id.toString())
            .setValue(user)
            .addOnSuccessListener {
                alertDialog.dismiss()
                showToast(requireContext(), "Account successfully created")
                Log.d(TAG, "createAccount: Account $user has successfully created")
                user?.let { appDatabase.userDao().addUser(it) }
                openHomePage()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "createAccount: $exception")
                Log.e(TAG, "createAccount: ${exception.message}")
                Log.e(TAG, "createAccount: ${exception.printStackTrace()}")
                dialogBinding.progressbar.visibility = View.INVISIBLE
                dialogBinding.notFoundIv.visibility = View.VISIBLE
                dialogBinding.loadingTv.text = "Something went wrong"
            }
    }

    private fun openHomePage() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        val bundle = Bundle()
        bundle.putParcelable(BundleKeys.USER, user)
        findNavController().navigate(R.id.homeFragment, bundle, navOptions)
    }

    private fun fieldsCorrect(): Boolean {
        unFilledFields = ArrayList()
        if (binding.fullNameEt.text.toString().isEmpty()) {
            showToast(requireContext(), "Ismingizni kiriting")
            return false
        }
        if (binding.roleSpinner.selectedItemPosition == 0) {
            binding.roleSpinner.performClick()
            unFilledFields.add("Rollardan birini tanlang")
            return false
        }
        return true
    }

    private fun setSpinner() {
        roleList = ArrayList()
        roleList.add("Foydalanuvchi rolini tanlang")
        roleList.add("Abituriyent")
        roleList.add("O'quvchi")
        roleList.add("O'qituvchi")
        val adapter = UserRoleAdapter(roleList, requireContext())
        binding.roleSpinner.adapter = adapter
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        val intent = signInClient.signInIntent
        showToast(requireContext(), "sign in with google")
        launcher.launch(intent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                showToast(requireContext(), result.resultCode.toString())
                Log.d(TAG, "${result.data}")
                Log.d(TAG, "${result.resultCode}")
            }
        }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val googleSignInAccount = task.getResult(ApiException::class.java)
            user?.email = googleSignInAccount.email
            binding.emailEt.hint = googleSignInAccount.email
            Log.d(TAG, "handleSignInResult: ${googleSignInAccount.account}")
            Log.d(TAG, "handleSignInResult: $googleSignInAccount")
        } else {
            Log.e(TAG, "handleSignInResult: ${task.exception}")
            Log.e(TAG, "handleSignInResult: ${task.exception?.message}")
            Log.e(TAG, "handleSignInResult: ${task.exception?.printStackTrace()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SavePersonalDataFragmen"
    }
}