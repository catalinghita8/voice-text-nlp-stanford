package com.example.arvoice.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.arvoice.R
import com.example.arvoice.domain.LogItem
import com.example.arvoice.utils.Constants.ARG_LOG_ITEM
import com.example.arvoice.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import java.util.*


class SplashActivity: AppCompatActivity() {
    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animationView.playAnimation()

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)

        scope.launch {
            val currentTime = Calendar.getInstance().timeInMillis
            viewModel.requestInitializeNLP()

            val logItem: LogItem
            val difference = (Calendar.getInstance().timeInMillis - currentTime) / 1000.0
            logItem = LogItem(
                difference.toLong(),
                "Initializing NLP pipeline  took: " + String.format("%.2f", difference) + "s"
            )

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra(ARG_LOG_ITEM, logItem)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }

}
