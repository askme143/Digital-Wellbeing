package com.yeongil.focusaid.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.focusaid.R
import com.yeongil.focusaid.databinding.FragmentNotificationActionBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerViewAdapter
import com.yeongil.focusaid.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.NotificationKeywordViewModel
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.AppListViewModel
import com.yeongil.focusaid.viewModelFactory.AppListViewModelFactory
import com.yeongil.focusaid.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class NotificationActionFragment : Fragment() {
    private var _binding: FragmentNotificationActionBinding? = null
    private val binding get() = _binding!!

    private val directions = NotificationActionFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val notiActionViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }
    private val notiKeywordViewModel by activityViewModels<NotificationKeywordViewModel>()
    private val appListViewModel by activityViewModels<AppListViewModel> {
        AppListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = notiActionViewModel

        with(binding.keywordRecyclerView) {
            if (this.adapter == null) {
                this.layoutManager = LinearLayoutManager(context)
                this.adapter = RecyclerViewAdapter(viewLifecycleOwner)
                this.itemAnimator = null
            }
        }

        notiActionViewModel.notiKeywordItemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { item ->
                notiKeywordViewModel.putNotiKeywordItem(item)
                findNavController().navigateSafe(
                    directions.actionNotificationActionFragmentToNotiKeywordDialog()
                )
            }
        }

        binding.addAppBtn.setOnClickListener {
            val appList = notiActionViewModel.getAppList()
            if (appList == null) appListViewModel.putAllApp()
            else appListViewModel.putAppList(appList)
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiAppListFragment())
        }
        binding.addKeywordBtn.setOnClickListener {
            notiKeywordViewModel.putNewNotiKeywordItem()
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiKeywordDialog())
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onStartGoBack() }
        binding.beforeBtn.setOnClickListener { onStartGoBack() }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(notiActionViewModel.getNotificationAction()!!)
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun onStartGoBack() {
        val originalAction = notiActionViewModel.originalAction
        val currentAction = notiActionViewModel.getNotificationAction()

        if (originalAction != currentAction) {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(R.layout.dialog_cancel_confirm)
            bottomSheetDialog.show()

            bottomSheetDialog.findViewById<Button>(R.id.complete_btn)!!
                .setOnClickListener {
                    bottomSheetDialog.dismiss()
                    goBack()
                }
            bottomSheetDialog.findViewById<Button>(R.id.cancel_btn)!!
                .setOnClickListener { bottomSheetDialog.dismiss() }
        } else {
            goBack()
        }
    }

    private fun goBack() {
        val goToEditFragment = notiActionViewModel.originalAction == null
        if (goToEditFragment) {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToActionEditFragment())
        } else {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }
    }
}