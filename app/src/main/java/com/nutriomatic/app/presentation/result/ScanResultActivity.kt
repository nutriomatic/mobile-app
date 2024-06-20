package com.nutriomatic.app.presentation.result

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.LocalData
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.NutritionScan
import com.nutriomatic.app.databinding.ActivityScanResultBinding
import com.nutriomatic.app.presentation.factory.ViewModelFactory

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private val args: ScanResultActivityArgs by navArgs()

    private val viewModel: ScanResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        viewModel.getNutritionScanById(args.scanId)
        viewModel.getNutritionScanByIdResponse.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                    }

                    is Result.Success -> {
                        setupScan(result.data.nutritionScan)
                    }

                    is Result.Error -> {
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

    private fun setupScan(nutritionScan: NutritionScan) {
        with(binding) {

            Glide.with(this@ScanResultActivity)
                .load(nutritionScan.snPicture)
                .into(ivScanPhoto)

            edtScanName.setText(nutritionScan.snProductName)
            btnOkay.setOnClickListener { finish() }
            btnSave.setOnClickListener { finish() }

            val gradeDrawableRes = LocalData.getGradeLabelByName(nutritionScan.snGrade)
            ivLabel.setImageResource(gradeDrawableRes)

            tvTotalCalory.text =
                getString(R.string.nutrition_value_kcal, nutritionScan.snEnergy.toDouble())
            tvTotalFat.text = getString(R.string.nutrition_value_g, nutritionScan.snFat.toDouble())
            tvProtein.text =
                getString(R.string.nutrition_value_g, nutritionScan.snProtein.toDouble())
            tvTotalCarbohydrate.text =
                getString(R.string.nutrition_value_g, nutritionScan.snCarbohydrate.toDouble())
            tvTotalSugar.text =
                getString(R.string.nutrition_value_g, nutritionScan.snSugar.toDouble())
            tvSodium.text =
                getString(R.string.nutrition_value_mg, nutritionScan.snSalt.toDouble().times(1000))


            topAppBar.setNavigationOnClickListener { onBackPressed() }
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        Snackbar.make(
                            this@ScanResultActivity,
                            binding.root,
                            "Edit clicked",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        true
                    }

                    else -> false
                }
            }
        }
    }
}