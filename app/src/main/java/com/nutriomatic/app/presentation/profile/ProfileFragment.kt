package com.nutriomatic.app.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select birth date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        with(binding) {
            txtBirthdayLayout.setEndIconOnClickListener {
                datePicker.show(childFragmentManager, "date_picker")
            }

            datePicker.addOnPositiveButtonClickListener {
                txtBirthdayInput.setText(datePicker.headerText)
            }

            txtBirthdayLayout.addOnEditTextAttachedListener {
                // If any specific changes should be done when the edit text is attached (and
                // thus when the trailing icon is added to it), set an
                // OnEditTextAttachedListener.

                // Example: The clear text icon's visibility behavior depends on whether the
                // EditText has input present. Therefore, an OnEditTextAttachedListener is set
                // so things like editText.getText() can be called.
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        // Lakukan tindakan ketika menu item edit diklik
                        makeToast("Edit clicked")
                        true
                    }

                    R.id.menu_logout -> {
                        makeToast("Logout clicked")
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun makeToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}