package com.example.repetitiontest.fragments.registration.set_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentSetPasswordBinding

class SetPasswordFragment : Fragment() {

    private var _binding: FragmentSetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetPasswordBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        private const val TAG = "SetPasswordFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetPasswordFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}