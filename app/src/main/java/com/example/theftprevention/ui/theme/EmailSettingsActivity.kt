package com.example.theftprevention

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmailSettingsActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_settings)

        emailInput = findViewById(R.id.editTextEmail)
        saveButton = findViewById(R.id.buttonSaveEmail)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", "")
        emailInput.setText(savedEmail)

        saveButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isNotEmpty() && email.contains("@")) {
                sharedPreferences.edit().putString("email", email).apply()
                Toast.makeText(this, "ایمیل مقصد ذخیره شد", Toast.LENGTH_SHORT).show()
                finish() // برگشت به صفحه قبل
            } else {
                Toast.makeText(this, "لطفاً یک ایمیل معتبر وارد کنید", Toast.LENGTH_SHORT).show()
            }
        }
    }
}