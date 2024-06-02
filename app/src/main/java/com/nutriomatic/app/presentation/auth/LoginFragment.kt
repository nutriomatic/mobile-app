package com.nutriomatic.app.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nutriomatic.app.R
import com.nutriomatic.app.data.pref.UserModel
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.databinding.FragmentLoginBinding
import com.nutriomatic.app.presentation.MainActivity
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = activity?.let { ViewModelFactory.getInstance(it) }!!
        setupAction()
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

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            viewModel.login(email, password)

            viewModel.dataLogin.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                activity, result.error, Toast.LENGTH_SHORT
                            ).show()
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


//                             If login is successful, start MainActivity
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            activity?.finish()

                            // Close AuthActivity
//                            val intent = Intent(activity, MainActivity::class.java)

//                            startActivity(intent)
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