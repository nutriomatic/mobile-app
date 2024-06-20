package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentRegisterBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.isValidEmail
import com.nutriomatic.app.presentation.helper.util.isValidName

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.tvRedirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = activity?.let { ViewModelFactory.getInstance(it) }!!

        setupAction()
        setupInput()
    }

    private fun setupInput() {
        binding.edtPassword.setTextInputLayout(binding.tilPassword)
        binding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val nameText = s.toString().trim()
                if (!isValidName(nameText)) {
                    binding.tilName.error = getString(R.string.minimum_name_length)
                } else {
                    binding.tilName.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s)) {
                    binding.tilEmail.error = getString(R.string.invalid_email_format)
                } else {
                    binding.tilEmail.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

//        binding.edtConfirmationPassword.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val confirmationPassword = s.toString()
//                val password = binding.edtPassword.text.toString()
//
//                if (password != confirmationPassword) {
//                    binding.tilConfirmationPassword.error =
//                        getString(R.string.passwords_do_not_match)
//                } else {
//                    binding.tilConfirmationPassword.error = null
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
    }


    private fun setupAction() {
        binding.btnRegister.setOnClickListener {

            val username = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            viewModel.register(username, email, password)

            viewModel.registerStatus.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            Snackbar.make(
                                requireView(),
                                result.data.message,
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}