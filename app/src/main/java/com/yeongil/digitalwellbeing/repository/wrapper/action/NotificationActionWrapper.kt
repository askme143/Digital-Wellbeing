package com.yeongil.digitalwellbeing.repository.wrapper.action

import com.yeongil.digitalwellbeing.data.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.repository.wrapper.ActionWrapper

class NotificationActionWrapper(notificationAction: NotificationAction) : ActionWrapper {
    override val title = "알림 처리"
    override val description = "${notificationAction.appList.joinToString("/")} 알림 숨김"
}