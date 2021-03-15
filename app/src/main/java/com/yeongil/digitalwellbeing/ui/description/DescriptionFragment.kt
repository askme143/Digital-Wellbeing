package com.yeongil.digitalwellbeing.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentDescriptionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.item.ACTIVITY_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.LOCATION_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.TIME_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    val directions = DescriptionFragmentDirections

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
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = descriptionViewModel

//        binding.title.setOnClickListener {
//            findNavController().navigateSafe(directions.actionDescriptionFragmentToRuleNameEditDialog())
//        }
//        binding.editBtn.setOnClickListener {
//            ruleEditViewModel.init(descriptionViewModel.rule.value!!)
//            findNavController().navigateSafe(directions.actionDescriptionFragmentToTriggerFragment())
//        }
        binding.finishBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionDescriptionFragmentToMainFragment())
        }
        binding.menuBtn.setOnClickListener { showPopup(it) }

        /* TODO: Observe item click event and navigate to it. */

        return binding.root
    }

    private fun showPopup(view: View) {
        PopupMenu(requireContext(), view).apply {
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_name -> {
                        findNavController().navigateSafe(directions.actionDescriptionFragmentToRuleNameEditDialog())
                        true
                    }
                    R.id.edit_trigger_action -> {
                        ruleEditViewModel.init(descriptionViewModel.rule.value!!)
                        findNavController().navigateSafe(directions.actionDescriptionFragmentToTriggerFragment())
                        true
                    }
                    else -> false
                }
            }

            inflate(R.menu.description_menu)
            show()
        }
    }
}