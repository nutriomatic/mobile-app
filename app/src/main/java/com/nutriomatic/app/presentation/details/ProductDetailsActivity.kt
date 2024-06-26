package com.nutriomatic.app.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.LocalData
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding
import com.nutriomatic.app.databinding.NutritionBottomSheetLayoutBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory
import com.nutriomatic.app.presentation.helper.util.formatCurrency
import java.net.URLEncoder


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
//                        val navHostFragment =
//                            supportFragmentManager.findFragmentById(R.id.main_navigation) as NavHostFragment
//                        val navController = navHostFragment.navController
//                        navController.popBackStack(R.id.storeFragment, false)
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

    private fun setupProduct(product: ProductsItem) {
        with(binding) {
            Glide.with(this@ProductDetailsActivity).load(product.productPicture)
                .placeholder(R.drawable.cendol).into(itemImage)
            itemTitle.text = product.productName
            itemPrice.text = formatCurrency(product.productPrice)
//            "Rp. ${product.productPrice.toString()}".also { itemPrice.text = it }
            itemDescription.text = product.productDesc
            val gradeResId = LocalData.getGradeLabelByName(product.productGrade)
            imgLabel.setImageResource(gradeResId)
            imgLabel.setOnClickListener {
                val modalBottomSheet = ModalBottomSheet(product)
                modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
            }

            topAppBar.setNavigationOnClickListener { onBackPressed() }
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_share -> {
                        val phoneNumber = "+6281234567890"
                        val encodedMessage = URLEncoder.encode(
                            "Hallo kak\nNew Product Launch\nIts is ${product.productName}\n${product.productDesc}\nHanya ${
                                formatCurrency(product.productPrice.toDouble())
                            }", "UTF-8"
                        )

                        val url =
                            "https://api.whatsapp.com/send?phone=$phoneNumber&text=$encodedMessage"

                        val i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(url))
                        startActivity(i)

//                        viewModel.getStoreById(product.storeId) // masih null
//                        viewModel.storeDetail.observe(this@ProductDetailsActivity) { result ->
//                            if (result != null) {
//                                when (result) {
//                                    is Result.Loading -> {
////                        binding.progressBar.visibility = View.VISIBLE
//                                    }
//
//                                    is Result.Success -> {
//                                        val url =
//                                            "https://api.whatsapp.com/send?phone=${result.data.store.storeContact}"
//                                        val i = Intent(Intent.ACTION_VIEW)
//                                        i.setData(Uri.parse(url))
//                                        startActivity(i)
//                                    }
//
//                                    is Result.Error -> {
////                        binding.progressBar.visibility = View.GONE
//                                        Toast.makeText(
//                                            this@ProductDetailsActivity,
//                                            result.error,
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            }
//                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

//    private fun showBottomSheetDialog(
//        productLemaktotal: Double,
//        productGaram: Double,
//        productKarbohidrat: Double,
//        productProtein: Double,
//        productServingsize: Int
//    ) {
//        val bottomSheetDialog = BottomSheetDialog(this)
//        val binding = NutritionBottomSheetLayoutBinding.inflate(layoutInflater)
//        bottomSheetDialog.setContentView(binding.root)
//
//
//        BottomSheetBehavior.from(binding.bottomSheetNutritionFact).apply {
//            this.state = BottomSheetBehavior.STATE_COLLAPSED
//        }
//
//        bottomSheetDialog.show()
//    }
}

class ModalBottomSheet(
    val product: ProductsItem,
) : BottomSheetDialogFragment() {
    private var _binding: NutritionBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = NutritionBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvTotalCalory.text = getString(R.string.nutrition_value_kcal, product.productEnergi)
            tvTotalFat.text = getString(R.string.nutrition_value_g, product.productLemaktotal)
            tvSaturatedFat.text = getString(R.string.nutrition_value_g, product.productLemakJenuh)
            tvProtein.text = getString(R.string.nutrition_value_g, product.productProtein)
            tvTotalCarbohydrate.text =
                getString(R.string.nutrition_value_g, product.productKarbohidrat)
            tvFiber.text = getString(R.string.nutrition_value_g, product.productFiber)
            tvTotalSugar.text = getString(R.string.nutrition_value_g, product.productGula)
            tvSodium.text = getString(R.string.nutrition_value_mg, product.productGaram.times(1000))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}