package com.nutriomatic.app.presentation.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Store
import com.nutriomatic.app.databinding.FragmentStoreBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.GridSpacingItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ProductDataAdapter
import com.nutriomatic.app.presentation.helper.adapter.ProductStorePagingDataAdapter

class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private var productAdapter: ProductDataAdapter? = null
    private var productStorePagingDataAdapter: ProductStorePagingDataAdapter? = null
    private var store_id: String? = null


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

        observeStore()

        with(binding) {
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        val navDirections =
                            StoreFragmentDirections.actionStoreFragmentToCreateStoreActivity(
                                store_id
                            )
                        findNavController().navigate(navDirections)
                        true
                    }

                    R.id.menu_payment_store -> {
                        val navDirections =
                            StoreFragmentDirections.actionStoreFragmentToPaymentActivity(
                                store_id
                            )
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


    private fun observeStore() {
        viewModel.store.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        setupStore(result.data.store)
//                        Hanya setup pagination ketika store_id pertama kali memiliki nilai
                        if (store_id == null) {
                            setupProductsStore(result.data.store.storeId)
                        }
                        setStoreId(result.data.store.storeId)
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

    private fun setStoreId(storeId: String) {
        store_id = storeId
    }

    private fun setupProductsStore(storeId: String) {
//        viewModel.getProductsByStore(storeId)
        setupProductPagination(storeId)
        viewModel.productsStore.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {

//                        setupProductPagination(result.data.products.toMutableList())
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

    private fun setupStore(store: Store) {
        with(binding) {
            tvStoreName.text = store.storeName
            tvStoreContact.text = store.storeContact
            tvUsernameStore.text = store.storeUsername
            tvStoreAddress.text = store.storeAddress
        }
    }

    //    private fun setupProductPagination(data: MutableList<ProductsItem>) {
    private fun setupProductPagination(storeId: String) {
//        if (data.isEmpty()) {
//            binding.messageEmpty.visibility = View.VISIBLE
//        } else {
//            binding.messageEmpty.visibility = View.GONE
//        }

        productStorePagingDataAdapter = ProductStorePagingDataAdapter(
            true,
            onIconClick = {
                val navDirections =
                    StoreFragmentDirections.actionStoreFragmentToAddProductActivity(
                        it.productId
                    )
                findNavController().navigate(navDirections)
//                view?.let { it1 ->
//                    Snackbar.make(it1, "Edit clicked", Snackbar.LENGTH_SHORT).show()
//                }
            })

        productStorePagingDataAdapter?.addLoadStateListener {
            val isEmpty =
                it.refresh is LoadState.NotLoading && productStorePagingDataAdapter?.itemCount == 0

            if (isEmpty) {
                binding.messageEmpty.visibility = View.VISIBLE
            } else {
                binding.messageEmpty.visibility = View.GONE
            }
        }

        binding.rvMyProducts.adapter = productStorePagingDataAdapter
        binding.rvMyProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rvMyProducts.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_item_offset),
                false
            )
        )
        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                binding.btnScrollTop.show()
            } else {
                binding.btnScrollTop.hide()
            }
        }
        binding.btnScrollTop.setOnClickListener {
            binding.nestedScrollView.smoothScrollTo(0, 0)
        }

        viewModel.getUserProductsPaging(storeId).observe(viewLifecycleOwner) {
            productStorePagingDataAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStore()
    }
}