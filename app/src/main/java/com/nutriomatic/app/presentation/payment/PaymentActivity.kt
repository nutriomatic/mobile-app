package com.nutriomatic.app.presentation.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.data.remote.api.response.TransactionsItem
import com.nutriomatic.app.databinding.ActivityPaymentBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.DefaultItemDecoration
import com.nutriomatic.app.presentation.helper.adapter.CheckoutAdapter
import com.nutriomatic.app.presentation.helper.util.formatCurrency
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
            binding.btnPay.text = formatCurrency(20000.0)
            binding.btnPay.setOnClickListener {
                val intent = Intent(this@PaymentActivity, UploadActivity::class.java)
                intent.putExtra("store_id", id)
                startActivity(intent)
                finish()
            }

            viewModel.getCheckout()

            viewModel.checkoutData.observe(this@PaymentActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            viewModel.getProductsByStore(id)
                            viewModel.productsStore.observe(this@PaymentActivity) { res ->
                                if (res != null) {
                                    when (res) {
                                        is Result.Loading -> {
                                            binding.progressBar.visibility = View.VISIBLE
                                        }

                                        is Result.Success -> {
                                            setupAdapter(
                                                result.data.transactions.toMutableList(),
                                                res.data.products.toMutableList(),
                                            )
                                            binding.progressBar.visibility = View.GONE
                                        }

                                        is Result.Error -> {
                                            binding.progressBar.visibility = View.GONE

                                            Snackbar.make(
                                                binding.root,
                                                res.error,
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
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

    private fun setupAdapter(
        transactionItem: MutableList<TransactionsItem>,
        productsItem: MutableList<ProductsItem>
    ) {
        var total = 0

        for (i in 0 until transactionItem.size) {
            total += transactionItem[i].tscPrice
        }

        for (i in 0 until productsItem.size) {
            for (j in 0 until transactionItem.size) {
                if (productsItem[i].productId == transactionItem[j].productId) {
                    transactionItem[j].productId = productsItem[i].productName
                    transactionItem[j].tscBukti = productsItem[i].productPicture
                }
            }
        }

        binding.btnPay.text = formatCurrency(total.toDouble()).toString()

        Log.d("TOTAL", total.toString())

        val adapter =
            CheckoutAdapter(
                listProduct = transactionItem
            )


        binding.rvProductPay.adapter = adapter
        binding.rvProductPay.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProductPay.addItemDecoration(
            DefaultItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.list_item_offset
                )
            )
        )
    }
}