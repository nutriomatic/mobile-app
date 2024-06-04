package com.nutriomatic.app.presentation.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding
import com.nutriomatic.app.databinding.BottomSheetLayoutBinding
import java.util.UUID

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val args: ProductDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val product = FakeDataSource.getProductById(UUID.fromString(args.productId))

        with(binding) {
            Glide.with(this@ProductDetailsActivity)
                .load(product?.photoUrl)
                .into(itemImage)
            itemTitle.text = product?.name
            itemPrice.text = product?.price.toString()
            imgLabel.setOnClickListener {
                showBottomSheetDialog()
            }

            topAppBar.setNavigationOnClickListener { onBackPressed() }
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_share -> {
                        Snackbar.make(
                            this@ProductDetailsActivity,
                            binding.root,
                            "Share: ${product!!.name}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        BottomSheetBehavior.from(binding.bottomSheetNutritionFact).apply {
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetDialog.show()
    }
}