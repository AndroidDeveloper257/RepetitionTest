package com.example.repetitiontest.fragments.enter_personal_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.FragmentEnterPersonalDataBinding
import com.example.repetitiontest.databinding.FragmentVerifyPhoneBinding

class EnterPersonalDataFragment : Fragment() {

    private var binding: FragmentEnterPersonalDataBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterPersonalDataBinding.inflate(layoutInflater)

        return binding?.root
    }

    companion object {
        private const val TAG = "EnterPersonalDataFragme"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.root
    }
}