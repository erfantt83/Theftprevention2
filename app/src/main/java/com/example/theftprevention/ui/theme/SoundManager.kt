package com.example.theftprevention.ui.theme

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.example.theftprevention.R
import com.example.theftprevention.passwordActivity

object SoundManager{
    private var mediaPlayer:MediaPlayer?=null
    fun playAlarm(context: Context){
        val audioManager=context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0
        )
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(context, R.raw.alarm_sound)
            mediaPlayer?.isLooping=true
            mediaPlayer?.start()
        }
        else if(mediaPlayer?.isPlaying==false){
            mediaPlayer?.start()
        }
    }
    fun stopAlarm(context: Context) {
        mediaPlayer?.let {
            if(it.isPlaying){
                it.stop()
            }
            it.release()
        }
       mediaPlayer=null
    }
    fun isPlaying():Boolean{
        return mediaPlayer?.isPlaying?:false
    }
}