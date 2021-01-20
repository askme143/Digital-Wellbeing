package com.yeongil.digitalwellbeing.utils

import android.content.SharedPreferences

class SequenceNumber(private val sharedPreferences: SharedPreferences) {
    private val key = "sequence number"

    fun getAndIncreaseSeqNumber(): Int {
        val curr = sharedPreferences.getInt(key, 1)
        with(sharedPreferences.edit()) {
            putInt(key, curr + 1)
            commit()
        }

        return curr
    }
}