package com.example.arvoice.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize data class LogItem(var timeStamp: Long, var content: String): Parcelable
