package com.example.repetitiontest.fragments.enter_personal_data

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.repetitiontest.const_values.BundleKeys
import com.example.repetitiontest.databinding.FragmentEnterPersonalDataBinding
import java.time.LocalDateTime

class EnterPersonalDataFragment : Fragment() {

    private var binding: FragmentEnterPersonalDataBinding? = null
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var fullName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterPersonalDataBinding.inflate(layoutInflater)
        val number = arguments?.getString(BundleKeys.PHONE_NUMBER)
        val pass = arguments?.getString(BundleKeys.PASSWORD)
        if (number != null && pass != null) {
            phoneNumber = number
            password = pass
        }

        return binding?.root
    }

    private fun isDateCorrect(year: Int, month: Int, date: Int): Boolean {
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (now.year < year) return false
        if (now.monthValue < month + 1) return false
        if (now.dayOfMonth < date) return false
        return true
    }

    companion object {
        private const val TAG = "EnterPersonalDataFragme"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.root
    }
}