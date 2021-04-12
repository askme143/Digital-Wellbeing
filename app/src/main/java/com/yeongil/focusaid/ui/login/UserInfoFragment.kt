package com.yeongil.focusaid.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.focusaid.MainActivity
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import com.yeongil.focusaid.databinding.FragmentUserInfoBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.UserInfoViewModel
import com.yeongil.focusaid.viewModelFactory.UserInfoViewModelFactory

class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private val directions = UserInfoFragmentDirections

    private val userInfoViewModel: UserInfoViewModel by activityViewModels {
        UserInfoViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.vm = userInfoViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        userInfoViewModel.submitEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                findNavController().navigateSafe(directions.actionUserInfoFragmentToUserInfoConfirmDialog())
            }
        }

        return binding.root
    }
}