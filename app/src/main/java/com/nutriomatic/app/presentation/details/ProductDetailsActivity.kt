package com.nutriomatic.app.presentation.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding

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
            itemDescription.text = args.productId
        }
    }
}