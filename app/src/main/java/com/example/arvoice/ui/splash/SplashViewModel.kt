package com.example.arvoice.ui.splash

import androidx.lifecycle.ViewModel
import com.example.arvoice.nlp.NLPClient
import com.example.arvoice.utils.Constants
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SplashViewModel : ViewModel(), CoroutineScope, ISplashViewModel {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private var nlpClient: NLPClient? = null

    override suspend fun requestInitializeNLP(){
        nlpClient = NLPClient.getInstance()
    }

}