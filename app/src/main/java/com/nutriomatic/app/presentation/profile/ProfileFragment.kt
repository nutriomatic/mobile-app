package com.nutriomatic.app.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Ekstensi fungsi untuk menampilkan Toast dengan mudah
    fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
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

        with(binding){
            txtBirthdayLayout.setEndIconOnClickListener {
                // Respond to end icon presses
                makeToast("Icon change birthday click")
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
                    else -> false
                }
            }
        }
    }

    private fun makeToast(msg :String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}