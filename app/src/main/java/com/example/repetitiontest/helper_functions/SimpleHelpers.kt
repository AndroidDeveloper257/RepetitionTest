package com.example.repetitiontest.helper_functions

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, toastMessage: String) {
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
}

fun phoneNumberToId(phoneNumber: String?): Long? {
    return phoneNumber?.substring(4)?.toLong()
}

fun idToPhoneNumber(id: Long?): String {
    return "+998$id"
}

fun makeupPhoneNumber(phoneNumber: String?): String {
    val part1 = phoneNumber?.substring(0, 4)
    val part2 = phoneNumber?.substring(4, 6)
    val part3 = phoneNumber?.substring(6, 9)
    val part4 = phoneNumber?.substring(9, 11)
    val part5 = phoneNumber?.substring(11)
    return "+998 93 189 53 05"
}

fun isPhoneNumberCorrect(phoneNumber: String): Boolean {
    if (!phoneNumber.startsWith("+998")) return false
    if (phoneNumber.length != 13) return false
    val substring = phoneNumber.substring(4, 6).toInt()
    if (substring != 33 &&
        substring != 88 &&
        substring != 90 &&
        substring != 91 &&
        substring != 93 &&
        substring != 94 &&
        substring != 95 &&
        substring != 97 &&
        substring != 99
    ) return false
    return true
}