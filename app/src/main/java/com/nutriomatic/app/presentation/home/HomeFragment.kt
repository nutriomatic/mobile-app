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
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.FragmentHomeBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ListProductAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var productAdapter: ListProductAdapter? = null

//    private val viewModel: AuthViewModel by viewModels {
//        ViewModelFactory.getInstance(requireActivity())
//    }

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

//    private val profileViewModel: ProfileViewModel by viewModels {
//        ViewModelFactory.getInstance(requireActivity())
//    }

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

        observeLiveData()

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

        homeViewModel.productsAdvertise.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        setupAdapter(result.data.products.toMutableList())
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

    private fun setupAdapter(data: MutableList<ProductsItem>) {
        val productAdapter = ListProductAdapter(data) {
            val navDirections =
                HomeFragmentDirections.actionHomeFragmentToProductDetailsActivity(it.productId)
            findNavController().navigate(navDirections)
//            view?.let { it1 -> Navigation.findNavController(it1).navigate(navDirections) }
        }


        with(binding) {
            rvProduct.adapter = productAdapter
            rvProduct.layoutManager = GridLayoutManager(activity, 2)
            rvProduct.addItemDecoration(
                GridSpacingItemDecoration(
                    2, resources.getDimensionPixelSize(R.dimen.grid_item_offset), false
                )
            )
        }

    }

    private fun observeLiveData() {
//        viewModel.getUserModel().observe(viewLifecycleOwner) {
//            binding.tvGreetUser.text = getString(R.string.greet_user, it.email)
//        }

        homeViewModel.detailProfile.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
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