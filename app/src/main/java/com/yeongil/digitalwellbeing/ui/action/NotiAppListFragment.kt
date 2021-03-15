package com.yeongil.digitalwellbeing.ui.action

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
import com.yeongil.digitalwellbeing.databinding.FragmentNotiAppListBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerViewAdapter
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppListViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppListViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory

class NotiAppListFragment : Fragment() {
    private var _binding: FragmentNotiAppListBinding? = null
    private val binding get() = _binding!!

    private val directions = NotiAppListFragmentDirections

    private val notiActionViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }
    private val appListViewModel by activityViewModels<AppListViewModel> {
        AppListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotiAppListBinding.inflate(inflater, container, false)
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

            findNavController().navigateSafe(directions.actionNotiAppListFragmentToNotificationActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            hideKeyboard()

            val appList = appListViewModel.getAppList()
            if (appList == null) notiActionViewModel.putAllApp()
            else notiActionViewModel.updateAppList(appList)
            findNavController().navigateSafe(directions.actionNotiAppListFragmentToNotificationActionFragment())
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