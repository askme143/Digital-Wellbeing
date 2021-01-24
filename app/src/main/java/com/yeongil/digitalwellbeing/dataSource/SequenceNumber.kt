package com.yeongil.digitalwellbeing.dataSource

import android.content.Context
import android.content.SharedPreferences

class SequenceNumber(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(
            "com.yeongil.digitalwellbeing.SEQUENCE_NUMBER",
            Context.MODE_PRIVATE
        )
    private val key = "sequence number"

    fun getAndIncreaseSeqNumber(): Int {
        val curr = sharedPref.getInt(key, 1)
        with(sharedPref.edit()) {
            putInt(key, curr + 1)
            commit()
        }

        return curr
    }
}