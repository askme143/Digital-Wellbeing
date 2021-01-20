package com.yeongil.digitalwellbeing.repository.wrapper.action

import com.yeongil.digitalwellbeing.data.dto.action.RingerAction
import com.yeongil.digitalwellbeing.repository.wrapper.ActionWrapper

class RingerActionWrapper(val ringerAction: RingerAction): ActionWrapper {
    override val title = "소리 모드 변경"
    override val description: String = ringerAction.ringerMode.toString()   //TODO: Mapping
}