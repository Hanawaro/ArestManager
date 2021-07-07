package com.speechpeach.arestmanager.utils

object ValueConstants {

    object ArestStatus {
        const val active = "Active"
        const val completed = "Completed"
        const val canceled = "Canceled"
    }

    object Organization {

        const val FSSP = 17
        const val FNS  = 39

        val codes = mapOf(
                FSSP to "FSSP",
                FNS to "FNS"
        )
    }

    object Retrofit {
        const val URL = "http://192.168.0.65/api/"
    }
}