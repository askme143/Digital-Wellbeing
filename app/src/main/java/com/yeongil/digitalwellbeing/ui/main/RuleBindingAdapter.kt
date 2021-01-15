package com.yeongil.digitalwellbeing.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo

object RuleBindingAdapter {
    @BindingAdapter("rule_info_list")
    @JvmStatic
    fun ruleListBind(recyclerView: RecyclerView, data: List<RuleInfo>?) {
        if (recyclerView.adapter == null) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = RuleInfoAdapter()
        }
        (recyclerView.adapter as RuleInfoAdapter).submitList(data)
    }
}