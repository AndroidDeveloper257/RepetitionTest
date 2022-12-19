package com.example.repetitiontest.helper_functions

import android.view.MotionEvent

fun wasSwipeToRightEvent(motionEvent: MotionEvent): Boolean {
    var downX: Float = 0F
    return when (motionEvent.action) {
        MotionEvent.ACTION_DOWN -> {
            downX = motionEvent.x
            false
        }
        MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
            downX - motionEvent.x > 0
        }
        else -> false
    }
}

fun wasSwipeToLeftEvent(motionEvent: MotionEvent): Boolean {
    var downX: Float = 0F
    return when (motionEvent.action) {
        MotionEvent.ACTION_DOWN -> {
            downX = motionEvent.x
            false
        }
        MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
            downX - motionEvent.x > 0
        }
        else -> false
    }
}