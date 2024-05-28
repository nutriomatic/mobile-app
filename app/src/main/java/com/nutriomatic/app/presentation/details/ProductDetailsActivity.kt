package com.nutriomatic.app.presentation.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding
import com.nutriomatic.app.databinding.BottomSheetLayoutBinding
import kotlin.random.Random

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    val args: ProductDetailsActivityArgs by navArgs()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.product_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                // Lakukan tindakan ketika menu Share ditekan
                Toast.makeText(this, "Share Click", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding) {
//            Glide.with(this@ProductDetailsActivity)
//                .load()
//            itemDescription.text = args.productId

            Glide.with(this@ProductDetailsActivity)
                .load("https://picsum.photos/seed/${Random.nextInt(1, 6)}/200/300")
                .transform(CenterCrop(), RoundedCorners(20))
                .into(itemImage)

            imgLabel.setOnClickListener {
                showBottomSheetDialog()
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