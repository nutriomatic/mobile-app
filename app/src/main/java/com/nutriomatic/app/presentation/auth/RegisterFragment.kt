package com.nutriomatic.app.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        setupInput()

    }

    private fun setupInput() {
//        setMyButtonEnable()

        binding.edtPassword.setTextInputLayout(binding.tilPassword)
//        binding.edtPassword.setButton(binding.btnRegister)

//        binding.edtConfirmationPassword.setTextInputLayout(binding.tilConfirmationPassword)
//        binding.edtConfirmationPassword.setButton(binding.btnRegister)

        binding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val nameText = s.toString().trim()
                if (nameText.length < 3) {
                    binding.tilName.error = "Name must be than 3 character"
//                    setMyButtonEnable()
                } else {
                    binding.tilName.error = null
                    binding.tilName.isErrorEnabled = false
//                    setMyButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (!s.toString().trim().matches(Regex(emailPattern))) {
                    binding.tilEmail.error = "Emalil not valid"
//                    setMyButtonEnable()
                } else {
                    binding.tilEmail.error = null
                    binding.tilEmail.isErrorEnabled = false
//                    setMyButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edtConfirmationPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val confirmationPassword = s.toString()
                val password = binding.edtPassword.getPassword()

                if (password != confirmationPassword) {
                    binding.tilConfirmationPassword.error = "Password not same"
//                    setMyButtonEnable()
                } else {
                    binding.tilConfirmationPassword.error = null
                    binding.tilEmail.isErrorEnabled = false
//                    setMyButtonEnable()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

//    private fun setMyButtonEnable() {
//        val nameValid = binding.tilName.error == null
//        val passValid = binding.edtPassword.getButtonIsValid()
//        val confirmPassValid = binding.tilConfirmationPassword.error == null
//        val isEmailValid = binding.tilEmail.error == null
//
//        binding.btnRegister.isEnabled = nameValid && isEmailValid && passValid && confirmPassValid
//    }


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