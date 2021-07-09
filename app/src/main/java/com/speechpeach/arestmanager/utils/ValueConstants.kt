package com.speechpeach.arestmanager.utils

object ValueConstants {

    object ArestStatus {
        const val active = "Active"
        const val completed = "Completed"
        const val canceled = "Canceled"

        val values = listOf(
                active,
                completed,
                canceled
        )
    }

    object UserDocumentType {
        const val passport = "Passport"
        const val internationalPassport = "International Passport"

        val values = listOf(
                passport,
                internationalPassport
        )

        val countNumberLimit = mapOf(
                passport to 6,
                internationalPassport to 6
        )

        val countSetLimit = mapOf(
                passport to 4,
                internationalPassport to 2
        )
    }

    object Organization {
        const val FSSP = 17
        const val FNS  = 39

        val codes = mapOf(
                FSSP to "FSSP",
                FNS to "FNS"
        )
    }

    object ArestCreatedMethod {
        const val chooseUser = "Choose user"
        const val createUser = "Create user"

        val values  = listOf(
                chooseUser,
                createUser
        )
    }

    object Retrofit {

        private const val isDebug = false

        private const val DEBUG_URL = "http://192.168.0.65/napi/"
        private const val PRODUCTION_URL = "http://31.132.135.190:8888/napi/"

        val URL = if(isDebug) DEBUG_URL else PRODUCTION_URL
    }
}