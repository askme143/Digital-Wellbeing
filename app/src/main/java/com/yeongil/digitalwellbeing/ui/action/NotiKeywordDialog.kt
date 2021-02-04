package com.yeongil.digitalwellbeing.ui.action

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogNotiKeywordBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationKeywordViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory

class NotiKeywordDialog : BottomSheetDialogFragment() {
    private var _binding: DialogNotiKeywordBinding? = null
    private val binding get() = _binding!!

    private val directions = NotiKeywordDialogDirections

    private val notiActionViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }
    private val notiKeywordViewModel by activityViewModels<NotificationKeywordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNotiKeywordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = notiKeywordViewModel

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiKeywordDialogToNotificationActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            notiActionViewModel.putKeywordItem(notiKeywordViewModel.getNotiKeywordItem())
            findNavController().navigateSafe(directions.actionNotiKeywordDialogToNotificationActionFragment())
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.keyword.requestFocus()
        binding.keyword.selectAll()
        val inputMethodManager =
            dialog?.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.keyword, 0)
    }
}