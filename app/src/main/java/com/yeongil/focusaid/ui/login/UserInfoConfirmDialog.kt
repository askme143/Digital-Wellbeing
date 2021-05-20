package com.yeongil.focusaid.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.MainActivity
import com.yeongil.focusaid.databinding.DialogUserInfoConfirmBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.UserInfoViewModel
import com.yeongil.focusaid.viewModelFactory.UserInfoViewModelFactory

class UserInfoConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogUserInfoConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = UserInfoConfirmDialogDirections

    private val userInfoViewModel: UserInfoViewModel by activityViewModels {
        UserInfoViewModelFactory(requireContext())
    }

    private val mainActivityIntent by lazy {
        Intent(requireActivity(), MainActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUserInfoConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = userInfoViewModel

        userInfoViewModel.confirmEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { confirmed ->
                if (confirmed) startActivity(mainActivityIntent)
                else findNavController().navigateSafe(directions.actionUserInfoConfirmDialogToUserInfoFragment())
            }
        }

        return binding.root
    }
}