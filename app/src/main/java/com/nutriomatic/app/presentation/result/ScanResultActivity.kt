package com.nutriomatic.app.presentation.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.local.LocalData
import com.nutriomatic.app.databinding.ActivityScanResultBinding
import java.util.UUID

class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding
    private val args: ScanResultActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val nutritionScan = FakeDataSource.getScanById(UUID.fromString(args.scanId))

        with(binding) {

            Glide.with(this@ScanResultActivity)
                .load(nutritionScan?.photoUrl)
                .into(ivScanPhoto)

            edtScanName.setText(nutritionScan?.name)
            btnOkay.setOnClickListener { finish() }
            btnSave.setOnClickListener { finish() }

            val gradeDrawableRes = LocalData.getGradeLabelByName(nutritionScan?.grade!!)
            ivLabel.setImageResource(gradeDrawableRes)

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