package com.nutriomatic.app.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nutriomatic.app.databinding.HistoryFilterBottomSheetLayoutBinding

class HistoryFilterBottomSheet(
    private val selected: List<Boolean>,
    private val listener: HistoryFragment.OnApplyFilterListener,
) : BottomSheetDialogFragment() {
    private var _binding: HistoryFilterBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = HistoryFilterBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipA.isChecked = selected[0]
        binding.chipB.isChecked = selected[1]
        binding.chipC.isChecked = selected[2]
        binding.chipD.isChecked = selected[3]

        binding.btnApply.setOnClickListener {
            listener.onApplyFilter(
                listOf(
                    binding.chipA.isChecked,
                    binding.chipB.isChecked,
                    binding.chipC.isChecked,
                    binding.chipD.isChecked
                )
            )
            dismiss()
        }
    }
}