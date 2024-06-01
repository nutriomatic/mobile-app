package com.nutriomatic.app.presentation.result

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.data.fake.FakeDataSource
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

            topAppBar.setNavigationOnClickListener { onBackPressed() }
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        Toast.makeText(this@ScanResultActivity, "Edit clicked", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }

                    else -> false
                }
            }
        }
    }
}