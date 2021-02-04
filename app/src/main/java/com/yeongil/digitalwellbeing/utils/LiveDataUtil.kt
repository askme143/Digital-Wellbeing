package com.yeongil.digitalwellbeing.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    combine: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = combine(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = combine(this.value, liveData.value)
    }
    return result
}