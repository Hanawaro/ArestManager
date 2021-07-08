package com.speechpeach.arestmanager.utils

import java.util.*

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)

fun Calendar.month() = this.get(Calendar.MONTH) + 1
fun Calendar.calendarMonth() = this.get(Calendar.MONTH)

fun Calendar.year() = this.get(Calendar.YEAR)


object QuickCalendar {

    fun get(): Calendar = Calendar.getInstance().apply {
        time = Date()
    }

    fun get(unixTime: Long): Calendar = Calendar.getInstance().apply {
        time = Date(unixTime * 1000)
    }

    fun get(dayOffset: Int, monthOffset: Int, yearOffset: Int) : Calendar = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.DAY_OF_MONTH, dayOffset)
        add(Calendar.MONTH, monthOffset)
        add(Calendar.YEAR, yearOffset)
    }

}
