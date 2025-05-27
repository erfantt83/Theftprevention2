package com.example.theftprevention

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.theftprevention.R

class BatteryReceiver(private val mediaPlayer: MediaPlayer,private val context: Context):BroadcastReceiver() {
    private var wasCharging=true
    override fun onReceive(context: Context?, intent: Intent?) {
        val status=intent?.getIntExtra("status",-1)?:return
        val isCharging=status==2 || status==5
        if(wasCharging && !isCharging){
            mediaPlayer.isLooping=true
            mediaPlayer.start()
            val intent=Intent(this.context,passwordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.context.startActivity(intent)
        }
        wasCharging=isCharging
    }
}