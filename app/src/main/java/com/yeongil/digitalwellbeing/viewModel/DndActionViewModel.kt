package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndActionDto
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID

class DndActionViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    fun init(rid: Int) {
        this.rid = rid
    }

    fun getDndAction() = DndActionDto(rid)
}