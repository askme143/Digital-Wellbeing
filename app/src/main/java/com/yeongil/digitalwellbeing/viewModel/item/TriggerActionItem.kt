package com.yeongil.digitalwellbeing.viewModel.item

import android.annotation.SuppressLint
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.DndAction
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import com.yeongil.digitalwellbeing.data.action.RingerAction
import com.yeongil.digitalwellbeing.data.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.*

const val LOCATION_TRIGGER_TITLE = "장소"
const val TIME_TRIGGER_TITLE = "시간"
const val ACTIVITY_TRIGGER_TITLE = "활동"
const val APP_BLOCK_ACTION_TITLE = "앱 사용 제한"
const val NOTIFICATION_ACTION_TITLE = "알림 처리"
const val DND_ACTION_TITLE = "방해 금지 모드"
const val RINGER_ACTION_TITLE = "소리 모드 변경"

class TriggerActionItem(val title: String, val description: String) {
    val isTrigger = when (title) {
        LOCATION_TRIGGER_TITLE -> true
        TIME_TRIGGER_TITLE -> true
        ACTIVITY_TRIGGER_TITLE -> true
        APP_BLOCK_ACTION_TITLE -> false
        NOTIFICATION_ACTION_TITLE -> false
        DND_ACTION_TITLE -> false
        RINGER_ACTION_TITLE -> false
        else -> false
    }

    val isAction = when (title) {
        LOCATION_TRIGGER_TITLE -> false
        TIME_TRIGGER_TITLE -> false
        ACTIVITY_TRIGGER_TITLE -> false
        APP_BLOCK_ACTION_TITLE -> true
        NOTIFICATION_ACTION_TITLE -> true
        DND_ACTION_TITLE -> true
        RINGER_ACTION_TITLE -> true
        else -> false
    }

    constructor(locationTrigger: LocationTrigger) : this(
        LOCATION_TRIGGER_TITLE,
        "${locationTrigger.locationName} ${locationTrigger.range}m 내에 있을 때"
    )

    constructor(timeTrigger: TimeTrigger) : this(
        TIME_TRIGGER_TITLE,
        "${
            TimeUtils.startEndMinutesToString(
                timeTrigger.startTimeInMinutes,
                timeTrigger.endTimeInMinutes
            )
        }\n${TimeUtils.repeatDayToString(timeTrigger.repeatDay)}"
    )

    constructor(activityTrigger: ActivityTrigger) : this(
        ACTIVITY_TRIGGER_TITLE,
        activityTrigger.activity
    )

    constructor(appBlockAction: AppBlockAction, pmRepo: PackageManagerRepository) : this(
        APP_BLOCK_ACTION_TITLE,
        appBlockAction.appBlockEntryList.joinToString(", ") {
            "${pmRepo.getLabel(it.packageName)} ${it.allowedTimeInMinutes}분 사용 시"
        }
    )

    constructor(notificationAction: NotificationAction, pmRepo: PackageManagerRepository) : this(
        NOTIFICATION_ACTION_TITLE,
        notificationAction.appList.joinToString(" / ") { pmRepo.getLabel(it) }
    )

    @Suppress("UNUSED_PARAMETER")
    constructor(dndAction: DndAction) : this(
        DND_ACTION_TITLE,
        "방해 금지 모드 실행"
    )

    constructor(ringerAction: RingerAction) : this(
        RINGER_ACTION_TITLE,
        when (ringerAction.ringerMode) {
            VIBRATE -> "진동 모드로 변경"
            RING -> "소리 모드로 변경"
            SILENT -> "무음 모드로 변경"
            else -> "ERROR"
        }
    )
}