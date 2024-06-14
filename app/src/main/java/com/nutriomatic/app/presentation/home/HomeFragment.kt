package com.nutriomatic.app.presentation.home

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
import com.nutriomatic.app.databinding.FragmentHomeBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ProductDataAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

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

        observeProfileLiveData()
        setupProductPagination()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchView.hide()
                val query = binding.searchView.text.toString().trim()
                Snackbar.make(
                    requireContext(), view, "Search query: $query", Snackbar.LENGTH_SHORT
                ).show()

                false
            }

            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_filter -> {
                        Snackbar.make(
                            requireContext(), view, "Filter clicked", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                true
            }

        }

//        homeViewModel.productsAdvertise.observe(viewLifecycleOwner) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//
//                    is Result.Success -> {
////                        setupAdapter(result.data.products.toMutableList())
//                        binding.progressBar.visibility = View.GONE
//                    }
//
//                    is Result.Error -> {
//                        binding.progressBar.visibility = View.GONE
//
//                        Snackbar.make(
//                            requireView(),
//                            result.error,
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }
    }

    private fun setupProductPagination() {
        val productAdapter = ProductDataAdapter {
            val navDirections =
                HomeFragmentDirections.actionHomeFragmentToProductDetailsActivity(it.productId)
            findNavController().navigate(navDirections)
        }


        with(binding) {
            rvProduct.adapter = productAdapter
            rvProduct.layoutManager = GridLayoutManager(activity, 2)
            rvProduct.addItemDecoration(
                GridSpacingItemDecoration(
                    2, resources.getDimensionPixelSize(R.dimen.grid_item_offset), false
                )
            )

            homeViewModel.advertisedProductPaging.observe(viewLifecycleOwner) {
                productAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

    }

    private fun observeProfileLiveData() {
        homeViewModel.detailProfile.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.tvGreetUser.text =
                            getString(R.string.greet_user, result.data.user.name)
                        binding.progressBar.visibility = View.GONE
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}