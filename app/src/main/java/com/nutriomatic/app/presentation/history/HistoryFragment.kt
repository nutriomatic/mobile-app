package com.nutriomatic.app.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.FragmentHistoryBinding
import com.nutriomatic.app.presentation.helper.DefaultItemDecoration

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
            rvScanHistory.addItemDecoration(DefaultItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_offset)))

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_sort -> {
                        Snackbar.make(view, "Sort clicked", Snackbar.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }
    }
}