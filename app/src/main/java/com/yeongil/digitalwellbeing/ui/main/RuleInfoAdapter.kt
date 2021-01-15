package com.yeongil.digitalwellbeing.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.databinding.ItemRuleInfoBinding

class RuleInfoAdapter : ListAdapter<RuleInfo, RuleInfoAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(ItemRuleInfoBinding.inflate(LayoutInflater.from(parent.context)))

        viewHolder.itemView.apply {
            this.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRuleInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RuleInfo){
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RuleInfo>() {
        override fun areItemsTheSame(oldItem: RuleInfo, newItem: RuleInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RuleInfo, newItem: RuleInfo): Boolean {
            return oldItem == newItem
        }
    }
}