package com.example.arvoice.logs

import com.example.arvoice.domain.LogItem

object LogsHelper {
    fun createLog(message: String) = LogItem(System.currentTimeMillis(), message)
}
