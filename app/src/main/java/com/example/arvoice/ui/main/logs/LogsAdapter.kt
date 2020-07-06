package com.example.arvoice.ui.main.logs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arvoice.R
import com.example.arvoice.domain.LogItem
import kotlinx.android.synthetic.main.log_item_layout.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class LogsAdapter: RecyclerView.Adapter<LogsAdapter.LogsAdapterViewHolder>() {

    var logs: ArrayList<LogItem> = arrayListOf()

    fun appendLog(log: LogItem) {
        Log.d("LOG_CLIENT", log.content)
        logs.add(log)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.log_item_layout, parent, false)
        return LogsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = logs.size

    override fun onBindViewHolder(holder: LogsAdapterViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    inner class LogsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val logContent: TextView = itemView.textLogMessage
        private val logTimestamp: TextView = itemView.textLogTime

        fun bind(log: LogItem) {
            logContent.text = log.content
            val format: DateFormat = SimpleDateFormat("hh:mm:ss")
            val value: String = format.format(log.timeStamp)
            logTimestamp.text = value
        }
    }

}