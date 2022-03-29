package com.example.helpers.util

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object TimeConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    fun converter(time: String): String {
        val d: ZonedDateTime = ZonedDateTime.parse(time)
        val prettyTime = PrettyTime(Locale.getDefault())
        return prettyTime.format(d)
    }
    fun convertDate(milliseconds: Long): String {
        return DateFormat.format("dd/MM/yyyy hh:mm a", milliseconds).toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): Long {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        return current.format(formatter).toLong()
    }
    @SuppressLint("SimpleDateFormat")
    fun getLongOfTime(dateTime: String): Long {
        return SimpleDateFormat("yyyy-MM-dd hh:mm").parse(dateTime)!!.time
    }
    fun getDate(timestamp: Long) :String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy",calendar).toString()
        return date
    }
    fun getDateTime(timestamp: String):String?{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.TAIWAN)
        val outputFormat = SimpleDateFormat("hh:mm-dd/MM/yyyy", Locale.TAIWAN)
        val date: Date = inputFormat.parse(timestamp) as Date
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }
}