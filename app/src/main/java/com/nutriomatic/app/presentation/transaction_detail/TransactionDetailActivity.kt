package com.nutriomatic.app.presentation.transaction_detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.databinding.ActivityTransactionDetailBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateString
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateTimeString
import com.nutriomatic.app.presentation.helper.util.convertToLocalTimeString
import java.util.Locale

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionDetailBinding
    private val args: TransactionDetailActivityArgs by navArgs()
    private val viewModel by viewModels<TransactionDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.getTransactionById(args.transactionId)
        viewModel.transaction.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Error -> {
                        Snackbar.make(this, binding.root, result.error, Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    is Result.Success -> {
                        val transaction = result.data.transaction
                        setupTransaction(transaction)
                    }
                }
            }
        }
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupTransaction(transaction: Transaction) {
        with(binding) {
            Glide.with(this@TransactionDetailActivity)
                .load(transaction.tscBukti)
                .placeholder(R.drawable.cendol)
                .into(ivProof)

            tvId.text = transaction.tscId
            tvCreatedAt.text = convertToLocalDateTimeString(transaction.createdAt)
            tvStatus.text = transaction.tscStatus.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            tvStatus.backgroundTintList = tvStatus.context.getColorStateList(
                when (transaction.tscStatus.lowercase()) {
                    "accepted", "paid" -> R.color.label_green
                    "declined" -> R.color.label_red
                    else -> R.color.label_yellow
                }
            )
            tvPrice.text = transaction.tscPrice.toString()
            tvVa.text = transaction.tscVirtualaccount.ifEmpty { "-" }
            tvStart.text = convertToLocalDateString(transaction.tscStart)
            tvEnd.text = convertToLocalTimeString(transaction.tscEnd)

            btnAccept.setOnClickListener { acceptTransaction() }
            btnDecline.setOnClickListener { declineTransaction() }

            if (args.forAdmin) {
                btnAccept.visibility = View.VISIBLE
                btnDecline.visibility = View.VISIBLE
            }
        }
    }

    private fun observeUpdateTransaction() {
        viewModel.updateTransactionResponse.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Error -> {
                        Snackbar.make(this, binding.root, result.error, Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    is Result.Success -> {
                        Snackbar.make(
                            this,
                            binding.root,
                            result.data.message.toString(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun acceptTransaction() {
        viewModel.updateTransaction(args.transactionId, "accepted")
        observeUpdateTransaction()
    }

    private fun declineTransaction() {
        viewModel.updateTransaction(args.transactionId, "declined")
        observeUpdateTransaction()
    }
}