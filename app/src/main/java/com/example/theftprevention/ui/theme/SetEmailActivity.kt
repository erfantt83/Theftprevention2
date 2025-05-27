package com.example.theftprevention

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_email)

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val btnSave = findViewById<Button>(R.id.btnSaveEmail)

        val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editEmail.setText(prefs.getString("recipient_email", ""))

        btnSave.setOnClickListener {
            val email = editEmail.text.toString().trim()
            if (email.contains("@") && email.contains(".")) {
                prefs.edit().putString("email", email).apply()
                Toast.makeText(this, "ایمیل ذخیره شد", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "ایمیل معتبر نیست", Toast.LENGTH_SHORT).show()
            }
        }
    }
}