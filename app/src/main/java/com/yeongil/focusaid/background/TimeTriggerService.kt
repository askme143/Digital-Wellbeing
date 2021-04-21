package com.yeongil.focusaid.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.repository.RuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class TimeTriggerService : LifecycleService() {
    private val ruleRepo by lazy {
        RuleRepository(
            SequenceNumber(this),
            RuleDatabase.getInstance(this).ruleDao()
        )
    }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }
    private val pendingIntent by lazy {
        Intent(this, TimeTriggerService::class.java).let {
            PendingIntent.getService(this, 0, it, 0)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.Default) {
            val rules = ruleRepo.getActiveRuleList()
            val (triggeredSet, minInterval) = checkTimeRules(rules)

            /* Set alarm manager */
            if (minInterval == null) {
                alarmManager.cancel(pendingIntent)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 60 * 1000,
                    pendingIntent
                )
            } else {
                alarmManager.cancel(pendingIntent)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + minInterval * 1000,
                    pendingIntent
                )
            }

            /* Pass results to MainService */
            val mainIntent = Intent(this@TimeTriggerService, MainService::class.java)
            val triggeredSetStr = Json.encodeToString(triggeredSet)
            mainIntent.putExtra(MainService.TIME_TRIGGERED_RULES_KEY, triggeredSetStr)
            mainIntent.action = MainService.TIME_TRIGGER
            startService(mainIntent)

            stopSelf(startId)
        }

        return START_REDELIVER_INTENT
    }

    private fun checkTimeRules(rules: List<Rule>): Pair<Set<Int>, Int?> {
        val timeRules = rules.filter { it.timeTrigger != null }
        if (timeRules.isEmpty()) return Pair(emptySet(), null)

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val yesterday = (today + 6) % 7
        val curr = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 +
                calendar.get(Calendar.MINUTE) * 60 +
                calendar.get(Calendar.SECOND)

        val secInDay = 24 * 60 * 60

        val checkResults = timeRules.map { rule ->
            with(rule.timeTrigger!!) {
                val start = startTimeInMinutes * 60
                val end = endTimeInMinutes * 60

                val repeatDayFromToday = repeatDay.drop(today) + repeatDay.slice(0 until today + 1)
                val untilNearestStart =
                    if (repeatDayFromToday[0] && curr < start) start - curr
                    else (repeatDayFromToday.indexOfFirst { it } - 1) * secInDay + (secInDay - curr) + start

                val (triggered, timeRemaining) =
                    when {
                        start == end -> {
                            when {
                                repeatDay[yesterday] && repeatDay[today] && curr - start in 0..29 ->
                                    Pair(false, 30 - curr + start)
                                curr < end && repeatDay[yesterday] ->
                                    Pair(true, end - curr)
                                start <= curr && repeatDay[today] ->
                                    Pair(true, secInDay - curr + end)
                                else ->
                                    Pair(false, untilNearestStart)
                            }
                        }
                        start < end -> {
                            when {
                                repeatDay[today] && curr in start until end ->
                                    Pair(true, end - curr)
                                else ->
                                    Pair(false, untilNearestStart)
                            }
                        }
                        else -> {
                            when {
                                curr < end && repeatDay[yesterday] ->
                                    Pair(true, end - curr)
                                start < curr && repeatDay[today] ->
                                    Pair(true, secInDay - curr + end)
                                else ->
                                    Pair(false, untilNearestStart)
                            }
                        }
                    }

                Triple(triggered, rule.ruleInfo.ruleId, timeRemaining)
            }
        }
        val triggeredSet = checkResults.filter { it.first }.map { it.second }.toSet()
        val minInterval = checkResults.minOf { it.third }

        return Pair(triggeredSet, minInterval)
    }
}