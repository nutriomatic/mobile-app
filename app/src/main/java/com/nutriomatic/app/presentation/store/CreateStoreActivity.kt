package com.nutriomatic.app.presentation.store

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Store
import com.nutriomatic.app.databinding.ActivityCreateStoreBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class CreateStoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoreBinding
    private val args: CreateStoreActivityArgs by navArgs()
    private val viewModel by viewModels<StoreViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.store.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        setupStore(result.data.store)
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        if (binding.txtNameInput.text.toString().isEmpty()) {
            setupAction()
        }
    }

    private fun setupStore(store: Store) {
        with(binding) {
            binding.btnSave.setText("Update Store")

            txtNameInput.setText(store.storeName)
            txtContactInput.setText(store.storeContact)
            txtUsernameInput.setText(store.storeUsername)
            txtAddressInput.setText(store.storeAddress)

            btnSave.setOnClickListener {

                val name = binding.txtNameInput.text.toString().trim()
                val username = binding.txtUsernameInput.text.toString().trim()
                val address = binding.txtAddressInput.text.toString().trim()
                val contact = binding.txtContactInput.text.toString().trim()

                viewModel.updateStore(name, username, address, contact)


                viewModel.updateStoreStatus.observe(this@CreateStoreActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                Snackbar.make(
                                    this@CreateStoreActivity,
                                    binding.root,
                                    result.data.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                                finish()
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE

                                Snackbar.make(
                                    this@CreateStoreActivity,
                                    binding.root,
                                    result.error,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnSave.setOnClickListener {

            val name = binding.txtNameInput.text.toString()
            val username = binding.txtUsernameInput.text.toString()
            val address = binding.txtAddressInput.text.toString()
            val contact = binding.txtContactInput.text.toString()

            viewModel.createStore(name, username, address, contact)

            viewModel.createStoreResponse.observe(this@CreateStoreActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE

                            finish()

                            Snackbar.make(
                                this@CreateStoreActivity,
                                binding.root,
                                result.data.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE

                            Snackbar.make(
                                this@CreateStoreActivity,
                                binding.root,
                                result.error,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}