package com.example.repetitiontest.const_values

object RoomFirebaseStatus {

    /**

     room ✅

     */
    const val ROOM_OK_FIREBASE_OK = 1
    /**
     * room ✅
     * firebase ✅
     * main page will be open
     */
    const val ROOM_OK_FIREBASE_NO = 2
    /**
     * room ✅
     * firebase ❌
     * sign up page will be open
     */



    /**

    room ❌

     */
    const val ROOM_NO_FIREBASE_OK = 3
    /**
     * room ❌
     * firebase ✅
     * sign in
     */
    const val ROOM_NO_FIREBASE_NO = 4
    /**
     * room ❌
     * firebase ❌
     * sign up
     */

}