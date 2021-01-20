package com.yeongil.digitalwellbeing.repository.wrapper.action

import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.repository.wrapper.ActionWrapper

class AppBlockActionWrapper(val appBlockAction: AppBlockAction) : ActionWrapper {
    override val title = "앱 사용 제한"
    override val description = appBlockAction.AppBlockEntries.joinToString(", ") {
        "${it.appName} ${it.allowedTimeInMinutes}분 사용 시"
    }
}