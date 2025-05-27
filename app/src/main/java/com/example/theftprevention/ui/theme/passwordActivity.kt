package com.example.theftprevention

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.theftprevention.ui.theme.SoundManager

class passwordActivity:AppCompatActivity(){
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        val editPassword=findViewById<EditText>(R.id.editPassword)
        val buttonSubmit=findViewById<Button>(R.id.buttonSubmit)
        val audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0
        )
        val sharedPrefs=getSharedPreferences("passwordPrefs",Context.MODE_PRIVATE)
        val savedPassword=sharedPrefs.getString("password","1234")
        buttonSubmit.setOnClickListener {
         val entered=editPassword.text.toString()
         if(entered==savedPassword){
             SoundManager.stopAlarm(this)
             val settingsPrefs=getSharedPreferences("settings",Context.MODE_PRIVATE)
             settingsPrefs.edit().putBoolean("battery_mode",false).apply()
             settingsPrefs.edit().putBoolean("shake_mode",false).apply()
             Toast.makeText(this,"Alarm stopped",Toast.LENGTH_SHORT).show()
             val intent=Intent(this,MainActivity::class.java)
             intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
             startActivity(intent)
             finish()
         }
         else{
              Toast.makeText(this,"Incorrect password",Toast.LENGTH_SHORT).show()
         }
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Enter correct password",Toast.LENGTH_SHORT).show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN || keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            val audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0
            )
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
