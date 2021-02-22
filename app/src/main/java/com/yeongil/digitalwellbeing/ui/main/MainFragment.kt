package com.yeongil.digitalwellbeing.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.background.MainService
import com.yeongil.digitalwellbeing.databinding.FragmentMainBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleInfoViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleInfoViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    val directions = MainFragmentDirections

    private val ruleInfoViewModel by activityViewModels<RuleInfoViewModel> {
        RuleInfoViewModelFactory(requireContext())
    }
    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val descriptionViewModel by activityViewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleInfoViewModel

        ruleInfoViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { rid ->
                descriptionViewModel.init(rid)
                findNavController().navigateSafe(directions.actionMainFragmentToDescriptionFragment())
            }
        }
        ruleInfoViewModel.itemClickActivate.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { (ruleId, activated) ->
                if (activated) {
                    Toast.makeText(context, "규칙이 활성화 됩니다. 조건을 충족하면 액션이 수행됩니다", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "규칙이 비활성화 됩니다. 조건을 충족하더라도 액션이 수행되지 않습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val intent = Intent(requireContext(), MainService::class.java)
                intent.action = MainService.RULE_CHANGE
                intent.putExtra(MainService.CHANGED_RULE_ID_KEY, ruleId)
                requireActivity().startService(intent)
            }
        }
        ruleInfoViewModel.itemClickNotiOnTriggerEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { (ruleId, notiOnTrigger) ->
                if (notiOnTrigger) {
                    Toast.makeText(context, "규칙의 조건이 만족되면 사용자 확인 후 액션이 실행됩니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "규칙의 조건이 만족되면 액션이 바로 실행됩니다.", Toast.LENGTH_SHORT).show()
                }

                val intent = Intent(requireContext(), MainService::class.java)
                intent.action = MainService.RULE_CHANGE
                intent.putExtra(MainService.CHANGED_RULE_ID_KEY, ruleId)
                requireActivity().startService(intent)
            }
        }
        ruleInfoViewModel.itemDeleteEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigateSafe(directions.actionMainFragmentToDeleteConfirmDialog())
            }
        }

        binding.addBtn.setOnClickListener {
            ruleEditViewModel.init()
            findNavController().navigateSafe(directions.actionMainFragmentToTriggerFragment())
        }

        return binding.root
    }
}