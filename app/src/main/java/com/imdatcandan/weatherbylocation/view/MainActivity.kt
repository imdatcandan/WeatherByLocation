package com.imdatcandan.weatherbylocation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.imdatcandan.weatherbylocation.R
import com.imdatcandan.weatherbylocation.databinding.MainActivityBinding
import com.imdatcandan.weatherbylocation.viewmodel.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), LocationListener {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainActivityBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        findLocation()

        viewModel.error.observe(this) {
            showErrorDialog(it)
        }

    }

    private fun findLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude.toLong()
        val longitude = location.longitude.toLong()
        viewModel.getWeatherByLocation(latitude, longitude)
    }

    override fun onResume() {
        super.onResume()
        findLocation()
    }

    private fun showErrorDialog(exception: Throwable) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_error_title)
            .setMessage(getString(R.string.dialog_error_message, exception.localizedMessage))
            .setPositiveButton(R.string.dialog_error_button) { _, _ ->
                findLocation()
            }.create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.toast_permission_granted, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private companion object {
        private const val LOCATION_PERMISSION_CODE = 1
    }
}
