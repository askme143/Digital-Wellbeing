package com.yeongil.focusaid.ui.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yeongil.focusaid.databinding.FragmentAppBlockListBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerViewAdapter
import com.yeongil.focusaid.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.AppListViewModel
import com.yeongil.focusaid.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.focusaid.viewModelFactory.AppListViewModelFactory

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
            hideKeyboard()

            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            hideKeyboard()

            val appList = appListViewModel.getAppList()
            if (appList == null) appBlockActionViewModel.putAllApp()
            else appBlockActionViewModel.updateAppBlockEntryList(appList)

            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        requireActivity().currentFocus?.clearFocus()
        inputMethodManager.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
    }
}