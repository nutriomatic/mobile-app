package com.nutriomatic.app.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

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

        with(binding) {
            val adapter = ListScanHistoryAdapter(FakeDataSource.generateFakeScans()) {
                val navDirections =
                    HistoryFragmentDirections.actionHistoryFragmentToScanResultActivity(it.id.toString())
                Navigation.findNavController(view).navigate(navDirections)
            }
            rvScanHistory.adapter = adapter
            rvScanHistory.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }
}