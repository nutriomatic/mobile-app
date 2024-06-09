package com.nutriomatic.app.presentation.details

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Product
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding
import com.nutriomatic.app.databinding.BottomSheetLayoutBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val args: ProductDetailsActivityArgs by navArgs()
    private val viewModel by viewModels<ProductDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        val product = FakeDataSource.getProductById(UUID.fromString(args.productId))
        viewModel.getProductById(args.productId)

        viewModel.detailProduct.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
//                        binding.progressBar.visibility = View.GONE
                        setupProduct(result.data.product)
                        val navHostFragment =
                            supportFragmentManager.findFragmentById(R.id.main_navigation) as NavHostFragment
                        val navController = navHostFragment.navController
                        navController.popBackStack(R.id.storeFragment, false)
                    }

                    is Result.Error -> {
//                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this, result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupProduct(product: Product) {
        with(binding) {
            Glide.with(this@ProductDetailsActivity)
                .load(product.productPicture)
                .into(itemImage)
            itemTitle.text = product.productName
            itemPrice.text = product.productPrice.toString()
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
                            "Share: ${product.productName}",
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