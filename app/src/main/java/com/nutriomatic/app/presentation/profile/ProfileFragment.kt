package com.nutriomatic.app.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.FragmentProfileBinding
import com.nutriomatic.app.presentation.auth.AuthActivity
import com.nutriomatic.app.presentation.auth.AuthViewModel
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select birth date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        observeLiveData()

        with(binding) {
            txtBirthdayLayout.setEndIconOnClickListener {
                datePicker.show(childFragmentManager, "date_picker")
            }

            datePicker.addOnPositiveButtonClickListener {
                txtBirthdayInput.setText(datePicker.headerText)
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        // Lakukan tindakan ketika menu item edit diklik
                        makeToast("Edit clicked")
                        true
                    }

                    R.id.menu_logout -> {
//                        makeToast("Logout clicked")
                        logout()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.txtNameInput.setText(user.token)
            binding.txtEmailInput.setText(user.email)
        }
    }

    private fun logout() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Logout")
                setMessage("Are you sure want to logout?")

                setPositiveButton("Lanjut") { _, _ ->
                    viewModel.logout()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }

                setNegativeButton("Cancel", null)

                create()
                show()
            }
        }
    }

    private fun makeToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}