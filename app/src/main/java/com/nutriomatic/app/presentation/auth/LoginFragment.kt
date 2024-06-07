package com.nutriomatic.app.presentation.auth

import android.content.Intent
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
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentLoginBinding
import com.nutriomatic.app.presentation.MainActivity
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.isValidEmail

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val emailValid = false
    private var passValid = false

    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels {
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

//            binding.btnLogin.setOnClickListener {
//                // Handle login logic here (e.g., authenticate user)
//
//                // If login is successful, start MainActivity
//                val intent = Intent(activity, MainActivity::class.java)
//                startActivity(intent)
//                activity?.finish() // Close AuthActivity
//            }

            val email = binding.edtEmailLog.text.toString()
            val password = binding.edtPassword.text.toString()

            viewModel.login(email, password)

            viewModel.dataLogin.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(requireView(), result.error, Snackbar.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE

                            result.data.token?.let { it1 ->
                                UserModel(
                                    email,
                                    it1
                                )
                            }?.let { it2 -> viewModel.saveSession(it2) }

                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
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