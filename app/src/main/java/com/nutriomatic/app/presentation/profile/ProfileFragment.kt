package com.nutriomatic.app.presentation.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.nutriomatic.app.presentation.helper.util.reduceFileSize
import com.nutriomatic.app.presentation.helper.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

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

    private var currentImageUri: Uri? = null

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

        setupProfile()

        with(binding) {
            txtBirthdayLayout.setEndIconOnClickListener {
                showDatePicker()
            }

            profileImage.setOnClickListener {
                startGallery()
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

            btnSave.setOnClickListener {
                updateProfile()
            }
        }
    }

    private fun updateProfile() {
        val name = binding.txtNameInput.text.toString()
        val email = binding.txtEmailInput.text.toString()
//            how to get the position of gender in drop down
//            val gender = getGenderValue()
        val gender = 1
        val telephone = binding.txtTelephoneInput.text.toString()
        val birthdate = binding.txtBirthdayInput.text.toString()
        val height = binding.txtHeightInput.text.toString().toInt()
        val weight = binding.txtWeightInput.text.toString().toInt()
        val weightGoal = binding.txtWeightGoalInput.text.toString().toInt()
//            val alType = binding.txtActivityLevelInput.text.toString()
//            val hgType = binding.txtHealthGoalInput.text.toString()

        val alType = 1
        val hgType = 1

        var filePart: MultipartBody.Part? = null
        currentImageUri?.let {uri->
            val imageFile = uriToFile(requireContext(), uri).reduceFileSize()
            val fileRequestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            filePart =
                MultipartBody.Part.createFormData("file", imageFile.name, fileRequestBody)
        }

        profileViewModel.updateProfile(
            name,
            email,
            gender,
            telephone,
            birthdate,
            height,
            weight,
            weightGoal,
            alType,
            hgType,
            filePart
        )

        profileViewModel.updateProfileResponse.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            getString(R.string.profile_update_success),
                            Snackbar.LENGTH_SHORT
                        ).show()
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

    private fun setupProfile() {
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
                            binding.txtTelephoneInput.setText(this.telp)
                            binding.txtBirthdayInput.setText(this.birthdate)
                            binding.txtGenderInput.setText(this.gender.toString())
                            binding.txtHeightInput.setText(this.height.toString())
                            binding.txtWeightInput.setText(this.weight.toString())
                            binding.txtWeightGoalInput.setText(this.weightGoal.toString())
                            binding.txtActivityLevelInput.setText(this.alType.toString())
                            binding.txtHealthGoalInput.setText(this.hgType.toString())
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

    private fun showDatePicker() {
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

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.profileImage.setImageURI(it)
        }
    }

    companion object {
        val gender = mapOf("Male" to 1, "Female" to 0)
    }
}