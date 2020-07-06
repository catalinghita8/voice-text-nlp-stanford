package com.example.arvoice.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arvoice.R
import com.example.arvoice.utils.Constants
import com.example.arvoice.domain.LogItem
import com.example.arvoice.ui.main.logs.LogsAdapter
import com.example.arvoice.ui.main.logs.MainLogger
import com.example.arvoice.ui.main.navigation.MainNavigation
import com.example.arvoice.ui.main.voice.VoiceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity(), MainNavigation, MainLogger {

    private val logsAdapter = LogsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToVoiceFragment()
        logsRecyclerView.adapter = logsAdapter
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layoutManager.stackFromEnd = true
        logsRecyclerView.layoutManager = layoutManager

        val initializationLog: LogItem? = intent.extras?.getParcelable(Constants.ARG_LOG_ITEM)
        initializationLog?.let { appendLog(it) }
    }

    override fun appendLog(log: LogItem) {
        logsAdapter.appendLog(log)
        logsRecyclerView?.scrollToPosition(logsAdapter.logs.size - 1)
    }

    override fun navigateToVoiceFragment() {
        var fragment = VoiceFragment()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameTarget)
        if(currentFragment != null && currentFragment is VoiceFragment)
            fragment = currentFragment
        navigateTo(fragment)
    }

    private fun navigateTo(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frameTarget, fragment).addToBackStack("tag")
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }

}
