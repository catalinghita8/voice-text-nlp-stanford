package com.example.arvoice.ui.main.voice

import android.text.Spannable
import com.example.arvoice.domain.LogItem

interface IVoiceViewModel {
    fun requestQuery(text: CharSequence): Pair<Spannable?, List<LogItem>>
}