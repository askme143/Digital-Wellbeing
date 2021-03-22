package com.yeongil.focusaid.viewModel.item

import android.text.Html
import android.text.Spanned
import com.yeongil.focusaid.R
import com.yeongil.focusaid.data.rule.action.*
import com.yeongil.focusaid.data.rule.action.RingerAction.RingerMode
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.data.rule.trigger.TimeTrigger
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.utils.*

const val LOCATION_TRIGGER_TITLE = "장소"
const val TIME_TRIGGER_TITLE = "시간"
const val ACTIVITY_TRIGGER_TITLE = "활동"
const val APP_BLOCK_ACTION_TITLE = "앱 사용 제한"
const val NOTIFICATION_ACTION_TITLE = "알림 처리"
const val DND_ACTION_TITLE = "방해 금지 모드"
const val RINGER_ACTION_TITLE = "소리 모드 변경"

@Suppress("DEPRECATION")
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
        APP_BLOCK_ACTION_TITLE -> R.drawable.ic_phone_block
        NOTIFICATION_ACTION_TITLE -> R.drawable.ic_notification
        DND_ACTION_TITLE -> R.drawable.ic_dnd
        RINGER_ACTION_TITLE -> R.drawable.ic_ringer_mode
        else -> R.drawable.ic_location
    }

    constructor(locationTrigger: LocationTrigger) : this(
        LOCATION_TRIGGER_TITLE,
        locationTrigger.let { triger ->
            val location = triger.locationName
            val range = triger.range
            val html = "$boldStart$location${boldEnd}을(를)$breakTag" +
                    "중심으로 $boldStart${range}m$boldEnd 내에 있을 때"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    constructor(timeTrigger: TimeTrigger) : this(
        TIME_TRIGGER_TITLE,
        timeTrigger.let { trigger ->
            val repeatDays = TimeUtils.repeatDayToString(trigger.repeatDay)
            val time = TimeUtils.startEndMinutesToString(
                timeTrigger.startTimeInMinutes,
                timeTrigger.endTimeInMinutes
            )
            val html = "${boldStart}매주 ${repeatDays}요일$breakTag" +
                    "$time$boldEnd"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    constructor(activityTrigger: ActivityTrigger) : this(
        ACTIVITY_TRIGGER_TITLE,
        activityTrigger.let { trigger ->
            val activity = when (trigger.activity) {
                DRIVE -> "자동차 운전"
                BICYCLE -> "자전거 운행"
                STILL -> "아무것도 하지 않을 때"
                else -> ""
            }
            val html = "$boldStart$activity$boldEnd 감지"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    constructor(appBlockAction: AppBlockAction, pmRepo: PackageManagerRepository) : this(
        APP_BLOCK_ACTION_TITLE,
        appBlockAction.let { action ->
            val apps =
                if (action.allAppBlock)
                    "모든 앱"
                else action.appBlockEntryList
                    .joinToString(", ") { pmRepo.getLabel(it.packageName) }
            val html = "${boldStart}제한한 앱$boldEnd$breakTag" + apps

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    constructor(notificationAction: NotificationAction, pmRepo: PackageManagerRepository) : this(
        NOTIFICATION_ACTION_TITLE,
        notificationAction.let { action ->
            val apps =
                if (action.allApp) "모든 앱"
                else action.appList.joinToString(", ") { pmRepo.getLabel(it) }
            val keywords = action.keywordList
                .joinToString(", ") {
                    val keyword = it.keyword
                    val inclusion = if (it.inclusion) "포함" else "미포함"
                    "$keyword (${inclusion})"
                }
            val html = "${boldStart}지정한 앱:$boldEnd $apps$breakTag" +
                    "${boldStart}지정한 키워드:$boldEnd $keywords"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    @Suppress("UNUSED_PARAMETER")
    constructor(dndAction: DndAction) : this(
        DND_ACTION_TITLE,
        "${boldStart}방해 금지 모드${boldEnd}를 실행합니다."
            .let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                else Html.fromHtml(it)
            }
    )

    constructor(ringerAction: RingerAction) : this(
        RINGER_ACTION_TITLE,
        ringerAction.let { action ->
            val ringerMode = when (action.ringerMode) {
                RingerMode.VIBRATE -> "진동 모드"
                RingerMode.RING -> "소리 모드"
                RingerMode.SILENT -> "무음 모드"
            }
            val html = "$boldStart$ringerMode${boldEnd}로 변경"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }
    )

    companion object {
        private const val breakTag = "<br />"
        private const val boldStart = "<b>"
        private const val boldEnd = "</b>"
    }
}