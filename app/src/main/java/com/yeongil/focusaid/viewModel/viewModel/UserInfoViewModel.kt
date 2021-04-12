package com.yeongil.focusaid.viewModel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import com.yeongil.focusaid.utils.Event

class UserInfoViewModel(
    private val userInfoPref: UserInfoPref
) : ViewModel() {
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    val nameErrorText = MutableLiveData<String>()
    val emailErrorText = MutableLiveData<String>()

    val submitEvent = MutableLiveData<Event<Unit>>()
    val confirmEvent = MutableLiveData<Event<Boolean>>()

    fun setUserInfo() {
        val name = this.name.value ?: ""
        val email = this.email.value ?: ""

        val nameError by lazy { name == "" }
        val emailError by lazy {
            email == "" || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        if (nameError)
            nameErrorText.value = "이름을 입력해주세요."
        else
            nameErrorText.value = ""

        if (emailError)
            emailErrorText.value = "이메일을 입력해주세요."
        else
            emailErrorText.value = ""


        if (!nameError && !emailError) {
            submitEvent.value = Event(Unit)
        }
    }

    fun confirm(confirmed: Boolean) {
        val name = this.name.value ?: ""
        val email = this.email.value ?: ""

        userInfoPref.setUserInfo(name, email)

        confirmEvent.value = Event(confirmed)
    }
}