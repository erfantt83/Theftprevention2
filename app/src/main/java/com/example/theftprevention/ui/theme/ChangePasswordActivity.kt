package com.example.theftprevention.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.theftprevention.MainActivity
import com.example.theftprevention.R

class ChangePasswordActivity:AppCompatActivity() {
    private lateinit var currentPasswordInput:EditText
    private lateinit var newPasswordInput:EditText
    private lateinit var btnSavePassword:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        currentPasswordInput=findViewById(R.id.editCurrentpassword)
        newPasswordInput=findViewById(R.id.editNewPassword)
        btnSavePassword=findViewById(R.id.btnSavePassword)
        val sharedPreferences=getSharedPreferences("passwordPrefs", Context.MODE_PRIVATE)
        val savadPassword=sharedPreferences.getString("password","1234")
        btnSavePassword.setOnClickListener {
            val current=currentPasswordInput.text.toString()
            val newPass=newPasswordInput.text.toString()
            if(current==savadPassword){
                sharedPreferences.edit().putString("password",newPass).apply()
                Toast.makeText(this,"Password updated successfully!",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Incorrect currect password!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}