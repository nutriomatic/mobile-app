package com.nutriomatic.app.presentation.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.FragmentStoreBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ListProductAdapter

class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private val listProduct: List<ProductsItem> = emptyList()
    private var productAdapter: ListProductAdapter? = null


    private val viewModel by viewModels<StoreViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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


        viewModel.products.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {

                        setupAdapter(result.data.products.toMutableList())


                        binding.progressBar.visibility = View.GONE

//                        Snackbar.make(
//                            requireView(),
//                            result.data.status.toString(),
//                            Snackbar.LENGTH_SHORT
//                        ).show()
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE

                        Snackbar.make(
                            requireView(),
                            result.error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        with(binding) {
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        val navDirections =
                            StoreFragmentDirections.actionStoreFragmentToCreateStoreActivity()
                        findNavController().navigate(navDirections)
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

    private fun setupAdapter(data: MutableList<ProductsItem>) {
        productAdapter = ListProductAdapter(
            data,
            true,
            onIconClick = {
                val navDirections =
                    StoreFragmentDirections.actionStoreFragmentToAddProductActivity(
                        it.productId
                    )
                findNavController().navigate(navDirections)
                view?.let { it1 ->
                    Snackbar.make(it1, "Edit clicked", Snackbar.LENGTH_SHORT).show()
                }
            })

        binding.rvMyProducts.adapter = productAdapter
        binding.rvMyProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rvMyProducts.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_item_offset),
                false
            )
        )
    }
}