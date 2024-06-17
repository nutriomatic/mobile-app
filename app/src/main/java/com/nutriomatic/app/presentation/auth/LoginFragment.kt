package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentLoginBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.isValidEmail

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels {
        factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.tvRedirectRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!

        setupAction()
        setupInput()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLog.text.toString()
            val password = binding.edtPassword.text.toString()

            viewModel.login(email, password)

            viewModel.loginData.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(requireView(), it.error, Snackbar.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE

                            viewModel.saveLoginData(it.data[0], email, it.data[1])

                            val destinationId = if (it.data[1] == "admin") {
                                R.id.action_loginFragment_to_adminActivity
                            } else {
                                R.id.action_loginFragment_to_mainActivity
                            }

                            findNavController().navigate(destinationId)

                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }


    private fun setupInput() {
        binding.edtPassword.setTextInputLayout(binding.tilPassword)
        binding.edtEmailLog.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s)) {
                    binding.tilEmailLog.error = getString(R.string.invalid_email_format)
                } else {
                    binding.tilEmailLog.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}