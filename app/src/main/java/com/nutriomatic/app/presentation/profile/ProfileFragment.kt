package com.nutriomatic.app.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentProfileBinding
import com.nutriomatic.app.presentation.auth.AuthViewModel
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.convertDateToString
import com.nutriomatic.app.presentation.helper.util.convertStringToMillis
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val authViewModel: AuthViewModel by viewModels {
        factory
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!

//        observeLiveData()
        setupProfile()
//        binding.txtBirthdayInput.setText(convertDateToString("2023 Januari 12"))


        with(binding) {
            txtBirthdayLayout.setEndIconOnClickListener {
                showDatePicker()
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        // Lakukan tindakan ketika menu item edit diklik
                        Snackbar.make(view, "Edit clicked", Snackbar.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menu_logout -> {
                        logout()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupProfile() {
//        authViewModel.saveUserModel()
//
//        profileViewModel.getUserModel().observe(viewLifecycleOwner) {
//            with(binding) {
//                txtNameInput.setText(it.name)
//                txtEmailInput.setText(it.email)
//                txtBirthdayInput.setText(it.birthdate)
//                txtGenderInput.setText(it.gender.toString())
//
//                txtHeightInput.setText(it.height.toString())
//                txtWeightInput.setText(it.weight.toString())
//                txtWeightGoalInput.setText(it.weightGoal.toString())
//
//                txtActivityLevelInput.setText(it.alDesc)
//                txtHealthGoalInput.setText(it.hgDesc)
//            }
//        }

        profileViewModel.detailProfile.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        result.data.user.apply {
                            binding.txtNameInput.setText(this.name)
                            binding.txtEmailInput.setText(this.email)
                            binding.txtBirthdayInput.setText(convertDateToString("2022-12-12"))
//                            binding.txtNameInput.setText(this.name)
//                            binding.txtNameInput.setText(this.name)
                        }
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

//    private fun observeLiveData() {
//        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
//            binding.txtNameInput.setText(user.token)
//            binding.txtEmailInput.setText(user.email)
//        }
//    }


    private fun logout() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle(getString(R.string.logout_dialog_title))
                setMessage(getString(R.string.logout_dialog_message))

                setPositiveButton(getString(R.string.dialog_continue)) { _, _ ->
                    authViewModel.logout()
                    findNavController().navigate(R.id.action_profileFragment_to_authActivity)
                    requireActivity().finish()
                }

                setNegativeButton(getString(R.string.dialog_cancel), null)

                create()
                show()
            }
        }
    }

    fun showDatePicker() {
        val dateInMillis = convertStringToMillis(binding.txtBirthdayInput.text.toString())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.birthday_datepicker_title))
            .setSelection(dateInMillis)
            .build()

        datePicker.show(childFragmentManager, "date_picker")

        datePicker.addOnPositiveButtonClickListener {
            binding.txtBirthdayInput.setText(datePicker.headerText)
        }
    }
}