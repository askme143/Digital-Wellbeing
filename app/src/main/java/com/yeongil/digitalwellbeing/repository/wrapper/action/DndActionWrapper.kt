package com.yeongil.digitalwellbeing.repository.wrapper.action

import com.yeongil.digitalwellbeing.data.dto.action.DndAction
import com.yeongil.digitalwellbeing.repository.wrapper.ActionWrapper

class DndActionWrapper(val dndAction: DndAction): ActionWrapper {
    override val title = "방해 금지 모드"
    override val description = "방해 금지 모드 실행"
}