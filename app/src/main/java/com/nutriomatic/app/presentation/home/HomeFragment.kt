package com.nutriomatic.app.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.FragmentHomeBinding
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_filter -> {
                    Toast.makeText(activity, "Filter clicked", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchView.hide()
                    val query = binding.searchView.text.toString().trim()
                    Toast.makeText(activity, query, Toast.LENGTH_SHORT).show()

                    false
                }

            val adapter = ListProductAdapter(FakeDataSource.generateFakeProduct()) {
                val navDirections =
                    HomeFragmentDirections.actionHomeFragmentToProductDetailsActivity(it.id.toString())
                Navigation.findNavController(view).navigate(navDirections)
            }
            rvProduct.adapter = adapter
            rvProduct.layoutManager = GridLayoutManager(activity, 2)
            rvProduct.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    resources.getDimensionPixelSize(R.dimen.grid_item_offset),
                    false
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}