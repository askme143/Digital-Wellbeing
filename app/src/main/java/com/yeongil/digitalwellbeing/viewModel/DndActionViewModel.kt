package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.DndAction

class DndActionViewModel : ViewModel() {
    fun getDndAction() = DndAction()
}