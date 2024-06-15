package com.nutriomatic.app.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.FragmentHistoryBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.DefaultItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ListScanHistoryAdapter

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListScanHistoryAdapter

    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selected.observe(viewLifecycleOwner) {
            filterScans(it)
        }

        with(binding) {
            adapter = ListScanHistoryAdapter(viewModel.scans) {
                val navDirections =
                    HistoryFragmentDirections.actionHistoryFragmentToScanResultActivity(it.id.toString())
                Navigation.findNavController(view).navigate(navDirections)
            }
            rvScanHistory.adapter = adapter
            rvScanHistory.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvScanHistory.addItemDecoration(DefaultItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_offset)))

            nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY > 0) {
                    binding.btnScrollTop.show()
                } else {
                    binding.btnScrollTop.hide()
                }
            }
            binding.btnScrollTop.setOnClickListener {
                binding.nestedScrollView.smoothScrollTo(0, 0)
            }

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_sort -> {
                        val bottomSheetDialog =
                            HistoryFilterBottomSheet(viewModel.selected.value!!, object :
                                OnApplyFilterListener {
                                override fun onApplyFilter(selected: List<Boolean>) {
                                    viewModel.updateSelected(selected)
                                }
                            })
                        bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun filterScans(selected: List<Boolean>) {
        val grades = listOf("A", "B", "C", "D")
        val selectedGrades = grades.filterIndexed { index, _ -> selected[index] }
        Snackbar.make(
            requireContext(),
            binding.root,
            "Filter applied: $selectedGrades",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    interface OnApplyFilterListener {
        fun onApplyFilter(selected: List<Boolean>)
    }
}