package com.example.repetitiontest.helper_functions

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, toastMessage: String) {
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}