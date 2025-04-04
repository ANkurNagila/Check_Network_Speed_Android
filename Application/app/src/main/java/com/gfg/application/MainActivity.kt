package com.gfg.application

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var networkSpeedChecker: SpeedChecker

    // UI elements
    private lateinit var tvDownloadSpeed: TextView
    private lateinit var tvUploadSpeed: TextView
    private lateinit var tvQuality: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnStartTest: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize NetworkSpeedChecker
        networkSpeedChecker = SpeedChecker(this)

        // Bind UI elements
        tvDownloadSpeed = findViewById(R.id.downloadSpeed)
        tvUploadSpeed = findViewById(R.id.uploadSpeed)
        tvQuality = findViewById(R.id.quality)
        tvStatus = findViewById(R.id.status)
        btnStartTest = findViewById(R.id.btnStartTest)
        progressBar = findViewById(R.id.progressBar)

        // Set up button click listener
        btnStartTest.setOnClickListener {
            startSpeedTest()
        }
    }

    private fun startSpeedTest() {
        // Update UI to show test is running
        progressBar.visibility = View.VISIBLE
        btnStartTest.isEnabled = false
        tvStatus.text = getString(R.string.testing_network_speed)

        // Run speed test in background
        lifecycleScope.launch {

            // Measure download speed
            tvStatus.text = getString(R.string.measuring_download_speed)
            val downloadSpeed = networkSpeedChecker.measureDownloadSpeed()

            // Measure upload speed
            tvStatus.text = getString(R.string.measuring_upload_speed)
            val uploadSpeed = networkSpeedChecker.measureUploadSpeed()

            // Get quality assessment
            val quality = networkSpeedChecker.getNetworkQuality(downloadSpeed)

            // Update UI with results
            runOnUiThread {

                tvDownloadSpeed.text = getString(R.string.download_speed, downloadSpeed)
                tvUploadSpeed.text = getString(R.string.upload_speed, uploadSpeed)
                tvQuality.text = getString(R.string.network_quality, quality)

                // Reset UI state
                progressBar.visibility = View.GONE
                btnStartTest.isEnabled = true
                tvStatus.text = getString(R.string.test_completed)
            }
        }
    }
}