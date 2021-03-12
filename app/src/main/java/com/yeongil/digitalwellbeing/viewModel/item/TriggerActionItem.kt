package com.yeongil.digitalwellbeing.viewModel.item

import android.text.Html
import android.text.Spanned
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.rule.action.*
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction.RingerMode
import com.yeongil.digitalwellbeing.data.rule.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.*

const val LOCATION_TRIGGER_TITLE = "장소"
const val TIME_TRIGGER_TITLE = "시간"
const val ACTIVITY_TRIGGER_TITLE = "활동"
const val APP_BLOCK_ACTION_TITLE = "앱 사용 제한"
const val NOTIFICATION_ACTION_TITLE = "알림 처리"
const val DND_ACTION_TITLE = "방해 금지 모드"
const val RINGER_ACTION_TITLE = "소리 모드 변경"

class TriggerActionItem(val title: String, val description: Spanned) {
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

    val resourceId = when (title) {
        LOCATION_TRIGGER_TITLE -> R.drawable.ic_location
        TIME_TRIGGER_TITLE -> R.drawable.ic_time
        ACTIVITY_TRIGGER_TITLE -> R.drawable.ic_activity
        APP_BLOCK_ACTION_TITLE -> R.drawable.ic_location
        NOTIFICATION_ACTION_TITLE -> R.drawable.ic_location
        DND_ACTION_TITLE -> R.drawable.ic_location
        RINGER_ACTION_TITLE -> R.drawable.ic_location
        else -> R.drawable.ic_location
    }

    constructor(locationTrigger: LocationTrigger) : this(
        LOCATION_TRIGGER_TITLE,
        ("$boldStart${locationTrigger.locationName}${boldEnd}을(를)${breakTag}" +
                "중심으로 $boldStart${locationTrigger.range}m$boldEnd 내에 있을 때")
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(timeTrigger: TimeTrigger) : this(
        TIME_TRIGGER_TITLE,
        ("${boldStart}매주 ${TimeUtils.repeatDayToString(timeTrigger.repeatDay)}요일$breakTag" +
                "${
                    TimeUtils.startEndMinutesToString(
                        timeTrigger.startTimeInMinutes,
                        timeTrigger.endTimeInMinutes
                    )
                }${boldEnd}")
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(activityTrigger: ActivityTrigger) : this(
        ACTIVITY_TRIGGER_TITLE,
        "$boldStart${
            when (activityTrigger.activity) {
                DRIVE -> "자동차 운전"
                BICYCLE -> "자전거 운행"
                STILL -> "아무것도 하지 않을 때"
                else -> ""
            }
        }$boldEnd 감지"
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(appBlockAction: AppBlockAction, pmRepo: PackageManagerRepository) : this(
        APP_BLOCK_ACTION_TITLE,
        (if (appBlockAction.allAppBlock) "모든 앱 실행 시"
        else appBlockAction.appBlockEntryList.joinToString(", ") {
            val label = pmRepo.getLabel(it.packageName)
            val allowedTime = it.allowedTimeInMinutes.let { min ->
                if (min == 0) "실행 시"
                else "${TimeUtils.minutesToTimeMinute(min)} 사용 시"
            }
            "$label $allowedTime"
        })
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(notificationAction: NotificationAction, pmRepo: PackageManagerRepository) : this(
        NOTIFICATION_ACTION_TITLE,
        (if (notificationAction.allApp) "모든 앱"
        else notificationAction.appList.joinToString(", ") { pmRepo.getLabel(it) } +
                " / " +
                when (notificationAction.handlingAction) {
                    0 -> "숨기기"
                    1 -> "진동"
                    2 -> "소리"
                    3 -> "무음"
                    else -> ""
                })
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    @Suppress("UNUSED_PARAMETER")
    constructor(dndAction: DndAction) : this(
        DND_ACTION_TITLE,
        "방해 금지 모드 실행"
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(ringerAction: RingerAction) : this(
        RINGER_ACTION_TITLE,
        when (ringerAction.ringerMode) {
            RingerMode.VIBRATE -> "진동 모드로 변경"
            RingerMode.RING -> "소리 모드로 변경"
            RingerMode.SILENT -> "무음 모드로 변경"
        }
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    companion object {
        private const val breakTag = "<br />"
        private const val boldStart = "<b>"
        private const val boldEnd = "</b>"
    }
}