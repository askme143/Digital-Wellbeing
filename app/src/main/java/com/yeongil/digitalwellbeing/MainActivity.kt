package com.yeongil.digitalwellbeing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeongil.digitalwellbeing.data.database.RuleDatabase
import com.yeongil.digitalwellbeing.data.dto.action.*
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        databaseTest()
    }

    private fun databaseTest() {
        baseContext.deleteDatabase("rule_db")

        launch {
            val dao = RuleDatabase.getInstance(applicationContext).ruleDao()
            val prettyJson = Json { prettyPrint = true }

            val sampleRuleInfo = RuleInfo(1, "sample", activated = false, notiOnTrigger = false)
            val sampleLocationTrigger = LocationTrigger(1, 1.0, 1.0)
            val sampleTimeTrigger = TimeTrigger(1, 1, 1)
            val sampleActivityTrigger = ActivityTrigger(1, "Driving")

            val sampleAppBlockAction = AppBlockAction(1, listOf(AppBlockEntry("yotube", 0)), 1)
            val sampleNotificationAction = NotificationAction(
                1,
                listOf("youtube"),
                listOf(
                    KeywordEntry("yeongil", true)
                ),
                1
            )
            val sampleDndAction = DndAction(1)
            val sampleRingerAction = RingerAction(1, 1)

            val sampleRule = Rule(
                sampleRuleInfo,
                sampleLocationTrigger,
                sampleTimeTrigger,
                sampleActivityTrigger,
                sampleAppBlockAction,
                sampleNotificationAction,
                sampleDndAction,
                sampleRingerAction
            )

            dao.insertRule(sampleRule)

            val ruleList = dao.getRuleList()
            Log.d("json", prettyJson.encodeToString(ruleList[0]))

            val sampleRuleInfo2 = RuleInfo(2, "sample", activated = false, notiOnTrigger = false)
            dao.insertRuleInfo(sampleRuleInfo2)
        }
    }
}