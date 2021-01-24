package com.yeongil.digitalwellbeing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
//            val dao = RuleDatabase.getInstance(applicationContext).ruleDao()
//            val prettyJson = Json { prettyPrint = true }
//
//            val sampleRuleInfo = RuleInfoDto(1, "sample", activated = false, notiOnTrigger = false)
//            val sampleLocationTrigger = LocationTriggerDto(1, 1.0, 1.0, 1, "sample")
//            val sampleTimeTrigger =
//                TimeTriggerDto(1, 1, 1, listOf(false, false, false, false, false, false, true))
//            val sampleActivityTrigger = ActivityTriggerDto(1, "Driving")
//
//            val sampleAppBlockAction = AppBlockActionDto(1, listOf(AppBlockEntry("yotube", 0)), 1)
//            val sampleNotificationAction = NotificationActionDto(
//                1,
//                listOf("youtube"),
//                listOf(
//                    KeywordEntryDto("yeongil", true)
//                ),
//                1
//            )
//            val sampleDndAction = DndActionDto(1)
//            val sampleRingerAction = RingerActionDto(1, 1)
//
//            val sampleRule = RuleDto(
//                sampleRuleInfo,
//                sampleLocationTrigger,
//                sampleTimeTrigger,
//                sampleActivityTrigger,
//                sampleAppBlockAction,
//                sampleNotificationAction,
//                sampleDndAction,
//                sampleRingerAction
//            )
//
//            dao.insertRule(sampleRule)
//
//            val ruleList = dao.getRuleList()
//            Log.d("json", prettyJson.encodeToString(ruleList[0]))
//
//            val sampleRuleInfo2 = RuleInfoDto(2, "sample", activated = false, notiOnTrigger = false)
//            dao.insertRuleInfo(sampleRuleInfo2)
        }
    }
}