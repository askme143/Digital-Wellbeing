package com.yeongil.digitalwellbeing.ui.action

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
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentNotificationActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerViewAdapter
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationKeywordViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiKeywordItemViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

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

        initViewModel()

        notiActionViewModel.notiKeywordItemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { id ->
                val clickedItem = notiActionViewModel.notiKeywordRecyclerItemList.value!!
                    .map { it.viewModel }
                    .filterIsInstance<NotiKeywordItemViewModel>()
                    .filter { it.id == id }[0]
                    .notiKeywordItem

                notiKeywordViewModel.init(clickedItem)
                findNavController().navigateSafe(
                    directions.actionNotificationActionFragmentToNotiKeywordDialog()
                )
            }
        }

        binding.addAppBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiAppListFragment())
        }
        binding.addKeywordBtn.setOnClickListener {
            notiKeywordViewModel.init()
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiKeywordDialog())
        }
        binding.handlingBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiHandlingDialog())
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onStartGoBack() }
        binding.beforeBtn.setOnClickListener { onStartGoBack() }
        binding.completeBtn.setOnClickListener {
            notiActionViewModel.editing = false
            ruleEditViewModel.addTriggerAction(notiActionViewModel.getNotificationAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        if (!notiActionViewModel.editing) {
            val action = ruleEditViewModel.editingRule.value?.notificationAction
            if (action != null) {
                notiActionViewModel.init(action)
            } else notiActionViewModel.init()
        }
    }

    private fun onStartGoBack() {
        if (notiActionViewModel.notiAppItemList.value?.isNotEmpty() == true ||
            notiActionViewModel.notiKeywordRecyclerItemList.value?.isNotEmpty() == true
        ) {
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
        notiActionViewModel.editing = false

        val goToEditFragment = ruleEditViewModel.editingRule.value?.notificationAction == null
        if (goToEditFragment) {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToActionEditFragment())
        } else {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }
    }
}