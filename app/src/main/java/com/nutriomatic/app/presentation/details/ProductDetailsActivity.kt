package com.nutriomatic.app.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.Product
import com.nutriomatic.app.databinding.ActivityProductDetailsBinding
import com.nutriomatic.app.databinding.NutritionBottomSheetLayoutBinding
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

    private fun setupProduct(product: Product) {
        with(binding) {
            Glide.with(this@ProductDetailsActivity).load(product.productPicture).into(itemImage)
            itemTitle.text = product.productName
            "Rp. ${product.productPrice.toString()}".also { itemPrice.text = it }
            itemDescription.text = product.productDesc

            imgLabel.setOnClickListener {
                val modalBottomSheet = ModalBottomSheet(
                    product.productLemaktotal,
                    product.productGaram,
                    product.productKarbohidrat,
                    product.productProtein,
                    product.productServingsize
                )
                modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
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
    private val productLemaktotal: Double,
    private val productGaram: Double,
    private val productKarbohidrat: Double,
    private val productProtein: Double,
    private val productServingsize: Int
) : BottomSheetDialogFragment() {
    private var _binding: NutritionBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NutritionBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtServingSizePerContInput.setText(productServingsize.toString())
        binding.txtFatInput.setText(productLemaktotal.toString())
        binding.txtCarboInput.setText(productKarbohidrat.toString())
        binding.txtProteinInput.setText(productProtein.toString())
        binding.txtSodiumInput.setText(productGaram.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}