package com.nutriomatic.app.presentation.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.FragmentStoreBinding
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration
import com.nutriomatic.app.presentation.home.ListProductAdapter

class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val adapter = ListProductAdapter(
                FakeDataSource.generateFakeProduct().take(5),
                true,
                onIconClick = {
                    val navDirections =
                        StoreFragmentDirections.actionStoreFragmentToAddProductActivity(it.id.toString())
                    findNavController().navigate(navDirections)
                    Toast.makeText(requireContext(), "Click: ${it.name}", Toast.LENGTH_SHORT).show()
                })
            rvMyProducts.adapter = adapter
            rvMyProducts.layoutManager = GridLayoutManager(activity, 2)
            rvMyProducts.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    resources.getDimensionPixelSize(R.dimen.grid_item_offset),
                    false
                )
            )

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        Toast.makeText(requireContext(), "Click edit", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
            fab.setOnClickListener {
                val navDirections =
                    StoreFragmentDirections.actionStoreFragmentToAddProductActivity(null)
                findNavController().navigate(navDirections)
            }

        }
    }
}