package com.example.arvoice.ui.main.voice


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.arvoice.R
import com.example.arvoice.domain.LogItem
import com.example.arvoice.logs.LogsHelper
import com.example.arvoice.ui.main.logs.MainLogger
import com.example.arvoice.ui.main.navigation.MainNavigation
import kotlinx.android.synthetic.main.fragment_voice.*


class VoiceFragment: Fragment() {

    private var speechRecognizer: SpeechRecognizer? = null

    private var isSpeechStarted = false

    private lateinit var viewModel: VoiceViewModel
    private var logger: MainLogger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VoiceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger = activity as MainLogger

        checkPermissions()
        listenToSpeechInput()

        btnSpeak.setOnClickListener {
            promptSpeechInput()
            addLog(LogsHelper.createLog("Recording sound..."))
        }

        txtSpeechInput.setOnClickListener {
            Toast.makeText(context, "I do something", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED)
            return
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(activity, "Record audio is required", Toast.LENGTH_LONG).show()
            } else {
                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
                ActivityCompat.requestPermissions(activity!!, permissions, 10)
            }
        }
    }

    private fun listenToSpeechInput() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onPartialResults(p0: Bundle?) { requestExtractQuery(p0, false) }

            override fun onEvent(p0: Int, p1: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onEndOfSpeech() { isSpeechStarted = false }

            override fun onError(p0: Int) { isSpeechStarted = false }

            override fun onResults(p0: Bundle?) {
                isSpeechStarted = false
                animationLoading.cancelAnimation()
                animationLoading.visibility = View.GONE
                speechRecognizer?.stopListening()

                requestExtractQuery(p0, true)
            }
        })
    }

    private fun promptSpeechInput() {
        isSpeechStarted = !isSpeechStarted

        if(isSpeechStarted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity?.application?.packageName?: "")
            speechRecognizer?.startListening(intent)
            animationLoading.playAnimation()
            animationLoading.visibility = View.VISIBLE
        }
        else {
            animationLoading.cancelAnimation()
            animationLoading.visibility = View.GONE
            speechRecognizer?.stopListening()
        }
    }

    private fun addLog(vararg logItems: LogItem) {
        for(log in logItems)
            logger?.appendLog(log)
    }

    private fun requestExtractQuery(bundle: Bundle?, finalResults: Boolean) {
        val type = if(finalResults) "Final" else "Partial"
        val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        data?.let { results ->
            txtSpeechInput.text = results[0]
            addLog(LogsHelper.createLog("Finished audio recording..."))
            addLog(LogsHelper.createLog("$type results: ${results[0]}"))

            val result: Pair<Spannable?, List<LogItem>> = viewModel.requestQuery(results[0])
            txtSpeechInput.text = result.first
            addLog(*result.second.toTypedArray())
            addLog(LogsHelper.createLog("Resolved query: ${viewModel.lastKnownQueryTagged}"))
        }
    }

}
