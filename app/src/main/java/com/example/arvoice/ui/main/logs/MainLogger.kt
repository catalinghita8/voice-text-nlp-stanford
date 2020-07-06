package com.example.arvoice.ui.main.logs

import com.example.arvoice.domain.LogItem

interface MainLogger {

    fun appendLog(log: LogItem)

}