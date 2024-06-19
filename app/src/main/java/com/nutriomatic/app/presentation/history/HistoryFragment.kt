package com.nutriomatic.app.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private val args: HistoryFragmentArgs by navArgs()
    private var adapter: ListScanHistoryAdapter? = null

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

        args.message.let {
            if (it != "NO_MESSAGE") {
                Snackbar.make(requireContext(), view, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        with(binding) {

            setupScanPagination()

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_sort -> {

                        val bottomSheetDialog =
                            HistoryFilterBottomSheet(viewModel.selected.value!!, object :
                                OnApplyFilterListener {
                                override fun onApplyFilter(selected: List<Boolean>) {
                                    viewModel.updateSelected(selected)
                                    viewModel.selected.observe(viewLifecycleOwner) { selections ->
                                        filterScans(selections)
                                    }
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

    private fun setupScanPagination() {
        adapter = ListScanHistoryAdapter {
            val navDirections =
                HistoryFragmentDirections.actionHistoryFragmentToScanResultActivity(it.snId)
            findNavController().navigate(navDirections)
        }

        viewModel.getNutritionScanPaging().observe(viewLifecycleOwner) {
            adapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }

        with(binding) {

            rvScanHistory.adapter = adapter
            rvScanHistory.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvScanHistory.addItemDecoration(DefaultItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_offset)))

            nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY > 0) {
                    btnScrollTop.show()
                } else {
                    btnScrollTop.hide()
                }
            }
            btnScrollTop.setOnClickListener {
                nestedScrollView.smoothScrollTo(0, 0)
            }
        }

    }

    private fun filterScans(selected: List<Boolean>) {
        val grades = listOf("A", "B", "C", "D")
        val selectedGrades = grades.filterIndexed { index, _ -> selected[index] }
        Toast.makeText(requireContext(), "Filter applied: $selectedGrades", Toast.LENGTH_SHORT)
            .show()
    }

    interface OnApplyFilterListener {
        fun onApplyFilter(selected: List<Boolean>)
    }
}