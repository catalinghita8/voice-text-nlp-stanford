package com.example.arvoice

import android.app.Application
import android.content.Context


class VoiceApplication : Application() {

    init { instance = this }

    companion object {
        var instance: VoiceApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}