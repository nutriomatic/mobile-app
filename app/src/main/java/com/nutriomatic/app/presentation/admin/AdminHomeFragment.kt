package com.nutriomatic.app.presentation.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentAdminHomeBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.DefaultItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.ListTransactionAdapter

class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListTransactionAdapter

    private val viewModel: AdminHomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProfileLiveData()


        with(binding) {
            progressBar.visibility = View.GONE

            adapter = ListTransactionAdapter {
                val navDirections =
                    AdminHomeFragmentDirections.actionAdminHomeFragmentToTransactionDetailActivity(
                        it.tscId, true
                    )
                findNavController().navigate(navDirections)
            }

            rvTransaction.adapter = adapter
            rvTransaction.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvTransaction.addItemDecoration(DefaultItemDecoration(resources.getDimensionPixelSize(R.dimen.list_item_offset)))

            nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY > 0) {
                    btnScrollTop.show()
                } else {
                    btnScrollTop.hide()
                }
            }
            btnScrollTop.setOnClickListener {
                binding.appBarLayout.setExpanded(true, true)
                binding.nestedScrollView.smoothScrollTo(0, 0)
            }
            viewModel.selected.observe(viewLifecycleOwner) { selected ->
                chipGroup.children.forEachIndexed { index, view ->
                    (view as Chip).isChecked = selected[index]
                }
            }
            chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                val selected = mutableListOf<Boolean>()
                for (i in 0 until group.childCount) {
                    selected.add(group.getChildAt(i).id in checkedIds)
                }
                viewModel.updateSelected(selected)
            }
        }

        viewModel.getAllTransactionsPaging().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeProfileLiveData() {
        viewModel.detailProfile.observe(viewLifecycleOwner) { result ->
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
}