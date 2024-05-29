package com.nutriomatic.app.presentation.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
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

        val nutritionScan = FakeDataSource.getScanById(UUID.fromString(args.scanId))

        with(binding) {

            Glide.with(this@ScanResultActivity)
                .load(nutritionScan?.photoUrl)
                .into(ivScanPhoto)

            edtScanName.setText(nutritionScan?.name)
            btnOkay.setOnClickListener { finish() }
            btnSave.setOnClickListener { finish() }
        }

    }
}