package com.example.repetitiontest.fragments.registration.set_personal_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.repetitiontest.databinding.FragmentSetPersonalDataBinding

class SetPersonalDataFragment : Fragment() {

    private var _binding: FragmentSetPersonalDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetPersonalDataBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        private const val TAG = "EnterPersonalDataFragme"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}