package com.example.theftprevention

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.theftprevention.GmailSender

class GpsLocationService : Service() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                val message = "موقعیت جدید:\nLatitude: $latitude\nLongitude: $longitude"
                val destinationEmail = sharedPreferences.getString("email", "") ?: ""

                if (destinationEmail.isNotEmpty()) {
                    Thread {
                        try {
                            val sender = com.example.theftprevention.GmailSender()
                            sender.sendMail(
                                "موقعیت مکانی دستگاه",
                                message,
                                "your_email@gmail.com", // ایمیل فرستنده
                                destinationEmail
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }.start()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "مجوز مکان داده نشده", Toast.LENGTH_SHORT).show()
            stopSelf()
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            60000, // هر 60 ثانیه
            10f,
            locationListener
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}