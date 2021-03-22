package com.yeongil.focusaid.viewModel.viewModel.action

import androidx.lifecycle.ViewModel
import com.yeongil.focusaid.data.rule.action.DndAction

class DndActionViewModel : ViewModel() {
    fun getDndAction() = DndAction()
}