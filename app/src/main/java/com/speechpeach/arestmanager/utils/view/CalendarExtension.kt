package com.speechpeach.arestmanager.utils.view

import java.util.*

fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)

fun Calendar.month() = this.get(Calendar.MONTH)

fun Calendar.year() = this.get(Calendar.YEAR)


object QuickCalendar {

    fun get(unixTime: Long): Calendar = Calendar.getInstance().apply {
        time = Date(unixTime * 1000)
    }

}
