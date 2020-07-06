package com.example.arvoice.ui.main.voice

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arvoice.VoiceApplication
import com.example.arvoice.R
import com.example.arvoice.domain.LogItem
import com.example.arvoice.nlp.NLPClient
import kotlinx.coroutines.launch

class VoiceViewModel: ViewModel(), IVoiceViewModel {

    private var nlpClient: NLPClient? = null

    init {
        viewModelScope.launch { nlpClient = NLPClient.getInstance() }
    }

    var lastKnownQueryTagged: String? = null

    override fun requestQuery(text: CharSequence): Pair<Spannable?, List<LogItem>> {
        val result = nlpClient!!.extractRootNoun(text)
        val rootNoun = result.first
        val logs = result.second
        rootNoun?.let {
            lastKnownQueryTagged = it
            val colorizedText = colorized(text.toString(), rootNoun, ContextCompat.getColor(VoiceApplication.applicationContext(), R.color.dark_blue))
            return Pair(colorizedText, logs)
        }
        lastKnownQueryTagged = null

        return Pair(text.toString().toSpannable(), logs)
    }

    private fun colorized(text: String, word: String, argb: Int): Spannable? {
        val spannable: Spannable = SpannableString(text)
        var substringStart = 0
        var start: Int
        while (text.indexOf(word, substringStart).also { start = it } >= 0) {
            spannable.setSpan(
                ForegroundColorSpan(argb), start, start + word.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            substringStart = start + word.length
        }
        return spannable
    }

}