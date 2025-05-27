package com.example.theftprevention

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.hardware.*
import android.location.LocationManager
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.theftprevention.ui.theme.ChangePasswordActivity
import com.example.theftprevention.EmailSettingsActivity
import com.example.theftprevention.GpsLocationService
import com.example.theftprevention.ui.theme.SoundManager
import kotlin.math.abs
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var batterySwitch: Switch
    private lateinit var shakeSwitch: Switch
    private lateinit var gpsSwitch: Switch
    private lateinit var btnChangePassword: Button
    private lateinit var btnEmailSettings: Button
    private lateinit var batteryReceiver: BroadcastReceiver
    private lateinit var sensorManager: SensorManager
    private var shakeListener: SensorEventListener? = null
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batterySwitch = findViewById(R.id.switch_battery_mode)
        shakeSwitch = findViewById(R.id.switch_shake_mode)
        gpsSwitch = findViewById(R.id.switch_gps_mode)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnEmailSettings = findViewById(R.id.btnEmailSettings)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        batterySwitch.isChecked = sharedPreferences.getBoolean("battery_mode", false)
        shakeSwitch.isChecked = sharedPreferences.getBoolean("shake_mode", false)
        gpsSwitch.isChecked = sharedPreferences.getBoolean("gps_mode", false)

        batterySwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("battery_mode", isChecked).apply()
        }

        shakeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("shake_mode", isChecked).apply()
            if (isChecked) startShakeDetection() else stopShakeDetection()
        }

        gpsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("gps_mode", isChecked).apply()
            if (isChecked) {
                if (!isLocationEnabled()) {
                    showLocationAlert()
                } else {
                    startService(Intent(this, GpsLocationService::class.java))
                    Toast.makeText(this, "GPS mode enabled", Toast.LENGTH_SHORT).show()
                }
            } else {
                stopService(Intent(this, GpsLocationService::class.java))
                Toast.makeText(this, "GPS mode disabled", Toast.LENGTH_SHORT).show()
            }
        }

        btnEmailSettings.setOnClickListener {
            startActivity(Intent(this, EmailSettingsActivity::class.java))
        }

        btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val status: Int = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val isCharging = status == BatteryManager.BATTERY_PLUGGED_USB || status == BatteryManager.BATTERY_PLUGGED_AC
                if (!isCharging && batterySwitch.isChecked) {
                    SoundManager.playAlarm(context)
                    startActivity(Intent(context, passwordActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            }
        }
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        if (shakeSwitch.isChecked) startShakeDetection()
    }

    private fun startShakeDetection() {
        val SHAKE_THRESHOLD = 1.0f
        var lastAcceleration = SensorManager.GRAVITY_EARTH
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        shakeListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    val acceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val delta = abs(acceleration - lastAcceleration)

                    if (delta > SHAKE_THRESHOLD && shakeSwitch.isChecked) {
                        setMaxVolume()
                        SoundManager.playAlarm(this@MainActivity)
                        startActivity(Intent(this@MainActivity, passwordActivity::class.java))
                    }

                    lastAcceleration = acceleration
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stopShakeDetection() {
        shakeListener?.let {
            sensorManager.unregisterListener(it)
        }
    }

    private fun setMaxVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showLocationAlert() {
        AlertDialog.Builder(this)
            .setTitle("موقعیت مکانی خاموش است")
            .setMessage("برای استفاده از GPS لطفاً آن را فعال کنید")
            .setPositiveButton("روشن کردن") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("لغو", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        stopShakeDetection()
    }
}