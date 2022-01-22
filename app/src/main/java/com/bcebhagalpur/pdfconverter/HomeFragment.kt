package com.bcebhagalpur.pdfconverter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var btnControl:Button

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        btnControl=view.findViewById(R.id.btnControl)

        btnControl.setOnClickListener {
           val  broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    val action = intent.action
                    val int: Int
                    if (Intent.ACTION_HEADSET_PLUG == action) {
                        int = intent.getIntExtra("state", -1)
                        if (int == 0) {
                            microphonePluggedIn = false
                            Toast.makeText(applicationContext, "Headphones not plugged in", Toast.LENGTH_LONG).show()
                        }
                        if (int == 1) {
                            microphonePluggedIn = true
                            Toast.makeText(applicationContext, "Headphones plugged in", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
            registerReceiver(broadcastReceiver, receiverFilter)
        }

        return view
    }

    private class MusicIntentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_HEADSET_PLUG) {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val state = intent.getIntExtra("state", -1)
                when (state) {
                    0 -> audioManager.setSpeakerphoneOn(true)
                    1 -> audioManager.setSpeakerphoneOn(false)
                    else -> audioManager.setSpeakerphoneOn(true)
                }
            }
        }
    }
}