package com.nutriomatic.app.presentation.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ActivityPaymentBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.adapter.CheckoutAdapter
import com.nutriomatic.app.presentation.store.PaymentViewModel

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private val args: PaymentActivityArgs by navArgs()
    private val viewModel by viewModels<PaymentViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            topAppBar.setNavigationOnClickListener { onBackPressed() }
        }

        args.storeId?.let { id ->
            binding.btnPay.setOnClickListener {
                val intent = Intent(this@PaymentActivity, UploadActivity::class.java)
                intent.putExtra("store_id", id)
                startActivity(intent)
                finish()
            }

            viewModel.getProductsByStore(id)

            viewModel.productsStore.observe(this@PaymentActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            setupAdapter(result.data.products.toMutableList())
                            binding.progressBar.visibility = View.GONE
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE

                            Snackbar.make(
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

    private fun setupAdapter(productsItem: MutableList<ProductsItem>) {
        val adapter =
            CheckoutAdapter(
                listProduct = productsItem
            )

        binding.rvProductPay.adapter = adapter
        binding.rvProductPay.layoutManager = GridLayoutManager(this, 1)
    }
}