package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockListBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerViewAdapter
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppListViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.AppListViewModelFactory

class AppBlockListFragment : Fragment() {
    private var _binding: FragmentAppBlockListBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockListFragmentDirections

    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val appListViewModel by activityViewModels<AppListViewModel> {
        AppListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appListViewModel

        with(binding.recyclerView) {
            if (this.adapter == null) {
                this.layoutManager = LinearLayoutManager(context)
                this.adapter = RecyclerViewAdapter(viewLifecycleOwner)
                this.itemAnimator = null
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            val appList = appListViewModel.getAppList()
            if (appList == null) appBlockActionViewModel.putAllApp()
            else appBlockActionViewModel.updateAppBlockEntryList(appList)

            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }

        return binding.root
    }
}