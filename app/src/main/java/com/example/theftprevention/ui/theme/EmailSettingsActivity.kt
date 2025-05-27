package com.example.theftprevention

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EmailSettingsActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_settings)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        saveButton = findViewById(R.id.buttonSaveEmail)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // بارگذاری اطلاعات قبلی
        emailEditText.setText(sharedPreferences.getString("email", ""))
        passwordEditText.setText(sharedPreferences.getString("email_password", ""))

        saveButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.contains("@") && password.length >= 6) {
                sharedPreferences.edit().putString("email", email).apply()
                sharedPreferences.edit().putString("email_password", password).apply()
                Toast.makeText(this, "اطلاعات ذخیره شد", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "ایمیل و رمز عبور معتبر وارد کنید", Toast.LENGTH_SHORT).show()
            }
        }
    }
}