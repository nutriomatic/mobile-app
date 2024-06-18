package com.nutriomatic.app.presentation.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.LocalData
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentProfileBinding
import com.nutriomatic.app.presentation.auth.AuthViewModel
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.convertStringToMillis
import com.nutriomatic.app.presentation.helper.util.millisToISOFormat
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

    private var genderCode = LocalData.GENDERS.first().code
    private var alCode = LocalData.ACTIVITY_LEVELS.first().type
    private var hgCode = LocalData.HEALTH_GOALS.first().type

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

        profileViewModel.getProfile()

        with(binding) {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        Snackbar.make(
                            view,
                            "Edit your profil and don't forget to save!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        editProfil()
                        true
                    }

                    R.id.menu_logout -> {
                        logout()
                        true
                    }

                    else -> false
                }
            }

            profileViewModel.detailProfile.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            result.data.user.apply {
                                genderCode = this.gender
                                alCode = this.alType
                                hgCode = this.hgType
                                Glide.with(requireContext())
                                    .load(profpic)
                                    .placeholder(R.drawable.profile_man)
                                    .error(R.drawable.profile_man)
                                    .into(binding.profileImage)
                                binding.txtNameInput.setText(this.name)
                                binding.txtEmailInput.setText(this.email)
                                binding.txtTelephoneInput.setText(this.telp)
                                binding.txtBirthdayInput.setText(this.birthdate)
                                val genderName =
                                    LocalData.getGenderNameByCode(requireContext(), this.gender)
                                binding.txtGenderInput.setText(genderName, false)
                                binding.txtHeightInput.setText(this.height.toString())
                                binding.txtWeightInput.setText(this.weight.toString())
                                binding.txtWeightGoalInput.setText(this.weightGoal.toString())
                                val alName = LocalData.getActivityLevelNameByCode(
                                    requireContext(),
                                    this.alType
                                )
                                binding.txtActivityLevelInput.setText(alName, false)
                                val hgName =
                                    LocalData.getHealthGoalNameByCode(requireContext(), this.hgType)
                                binding.txtHealthGoalInput.setText(hgName, false)
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

            btnSave.setOnClickListener {
                updateProfile()
                enabledEditProfil()
            }
        }
    }

    private fun enabledEditProfil() {
        with(binding) {
            txtNameInput.isEnabled = false
            txtEmailInput.isEnabled = false
            txtTelephoneInput.isEnabled = false
            txtGenderInput.isEnabled = false
            txtHeightInput.isEnabled = false
            txtWeightInput.isEnabled = false
            txtWeightGoalInput.isEnabled = false
            profileImage.isClickable = false
            txtBirthdayInput.isEnabled = false
            txtActivityLevelInput.isEnabled = false
            txtHealthGoalInput.isEnabled = false
            binding.btnSave.visibility = View.GONE
        }
    }

    private fun editProfil() {
        with(binding) {
            txtNameInput.isEnabled = true
            txtEmailInput.isEnabled = true
            txtTelephoneInput.isEnabled = true
            txtGenderInput.isEnabled = true
            txtHeightInput.isEnabled = true
            txtWeightInput.isEnabled = true
            txtWeightGoalInput.isEnabled = true
            profileImage.isClickable = true
            txtBirthdayInput.isEnabled = true
            txtActivityLevelInput.isEnabled = false
            txtHealthGoalInput.isEnabled = false

            btnSave.visibility = View.VISIBLE

            txtBirthdayInput.setOnClickListener {
                showDatePicker()
            }
            txtBirthdayLayout.setEndIconOnClickListener {
                showDatePicker()
            }

            val genders = LocalData.getGenderNames(requireContext())
            val genderAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
            txtGenderInput.setAdapter(genderAdapter)

            val activityLevels = LocalData.getActivityLevelNames(requireContext())
            val activityLevelAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                activityLevels
            )
            txtActivityLevelInput.setAdapter(activityLevelAdapter)

            val healthGoals = LocalData.getHealthGoalNames(requireContext())
            val healthGoalAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                healthGoals
            )
            txtHealthGoalInput.setAdapter(healthGoalAdapter)

            txtGenderInput.setOnItemClickListener { _, _, position, _ ->
                genderCode = LocalData.GENDERS[position].code
            }

            txtActivityLevelInput.setOnItemClickListener { _, _, position, _ ->
                alCode = LocalData.ACTIVITY_LEVELS[position].type
            }

            txtHealthGoalInput.setOnItemClickListener { _, _, position, _ ->
                hgCode = LocalData.HEALTH_GOALS[position].type
            }

            profileImage.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun updateProfile() {
        val name = binding.txtNameInput.text.toString()
        val email = binding.txtEmailInput.text.toString()
        val gender = genderCode
        val telephone = binding.txtTelephoneInput.text.toString()
        val birthdate = binding.txtBirthdayInput.text.toString()
        val height = binding.txtHeightInput.text.toString().toInt()
        val weight = binding.txtWeightInput.text.toString().toInt()
        val weightGoal = binding.txtWeightGoalInput.text.toString().toInt()
        val alType = alCode
        val hgType = hgCode

        var filePart: MultipartBody.Part? = null
        currentImageUri?.let { uri ->
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
                        profileViewModel.getProfile()
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
            binding.txtBirthdayInput.setText(millisToISOFormat(it))
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
}