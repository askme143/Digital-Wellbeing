package com.yeongil.focusaid.viewModel.viewModel

import androidx.lifecycle.*
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.utils.combineWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppBlockViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    var blockingAppKey = ""
        private set

    private val packageName = MutableLiveData<String>()
    val appLabel = packageName.map { pmRepo.getLabel(it) }

    private val isClose = MutableLiveData<Boolean>()

    private val _countDown = MutableLiveData(10)
    val tempUseBtnText = _countDown.combineWith(isClose) { int, bool -> Pair(int, bool) }
        .map { (sec, bool) ->
            val countDown = sec ?: 10
            val isClose = bool ?: true

            val tempUseText = if (isClose) "임시 사용" else "이번 사용만 경고 알림을 출력하지 않습니다."
            val countDownText = if (countDown == 0) "" else "\n(${countDown})"

            tempUseText + countDownText
        }
    val tempUseEnabled = _countDown.map { it == 0 }

    fun putPackageName(packageName: String) {
        this.packageName.value = packageName
        _countDown.value = 10

        viewModelScope.launch {
            for (i in 1..10) {
                delay(1 * 1000)
                withContext(Dispatchers.Main) {
                    _countDown.value = (_countDown.value ?: 10) - 1
                }
            }
        }
    }

    fun putBlockingAppKey(blockingAppKey: String) {
        this.blockingAppKey = blockingAppKey
    }

    fun setClose() = run { isClose.value = true }
    fun setAlert() = run { isClose.value = false }
}