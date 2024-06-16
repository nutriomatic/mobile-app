package com.nutriomatic.app.presentation.transaction_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.ActivityTransactionDetailBinding

class TransactionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionDetailBinding
    private val args: TransactionDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val transaction = FakeDataSource.getTransactionById(args.transactionId)

        with(binding) {
            Glide.with(this@TransactionDetailActivity)
                .load(transaction?.paymentProof)
                .placeholder(R.drawable.cendol)
                .into(ivProof)

            tvId.text = transaction?.id
        }
    }
}