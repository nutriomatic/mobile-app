package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentRegisterBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: ViewModelFactory
    private val viewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.tvRedirectLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = activity?.let { ViewModelFactory.getInstance(it) }!!


//        setupView()
        setupAction()
//        setupInput()

    }

//    private fun setupInput() {
//        setMyButtonEnable()
//
//        binding.passwordEditText.setTextInputLayout(binding.passwordEditTextLayout)
//        binding.passwordEditText.setButton(binding.signupButton)
//
//
//        binding.emailEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
//                if (!s.toString().trim().matches(Regex(emailPattern))) {
//                    binding.emailEditTextLayout.error = getString(R.string.error_email)
//                } else {
//                    binding.emailEditTextLayout.error = null
//                    setMyButtonEnable()
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        setMyButtonEnable()
//    }
//
//    private fun setMyButtonEnable() {
//        val passValid = binding.passwordEditText.getButtonIsValid()
//        val isEmailValid = binding.emailEditTextLayout.error == null
//        binding.signupButton.isEnabled = isEmailValid && passValid
//    }

//    private fun setupView() {
//        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.hide()
//    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {

            val username = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmationPassword.text.toString()

            viewModel.register(username, email, password)

            viewModel.registerStatus.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                activity,
                                "Selamat, " + result.data.message,
                                Toast.LENGTH_SHORT
                            ).show()

//                            activity?.let { it1 ->
//                                AlertDialog.Builder(it1).apply {
//                                    setTitle("Succcesfully registration")
//                                    setMessage("Selamat, Registrasi Berhasil")
//
//                                    setPositiveButton("Lanjut") { _, _ ->
//                                        startActivity(
//                                            Intent(
//                                               activity, LoginFragment::class.java
//                                            )
//                                        )
//                                        finish()
//                                    }
//                                    create()
//                                    show()
//                                }
//                            }
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                activity, result.error, Toast.LENGTH_SHORT
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