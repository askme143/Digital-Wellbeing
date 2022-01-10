package com.yeongil.focusaid.dataSource

import android.content.Context
import android.content.SharedPreferences
import com.yeongil.focusaid.BuildConfig

class SequenceNumber(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.SEQUENCE_NUMBER",
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