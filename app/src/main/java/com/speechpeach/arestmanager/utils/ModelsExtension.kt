package com.speechpeach.arestmanager.utils

import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User

fun String.toArestStatusType()
    = when(this) {
        Arest.Type.Active.toString() -> Arest.Type.Active
        Arest.Type.Completed.toString() -> Arest.Type.Completed
        Arest.Type.Canceled.toString() -> Arest.Type.Canceled

        else -> throw IllegalArgumentException()
    }

fun String.toUserDocumentType()
    = when(this) {
        User.Type.Passport.toString() -> User.Type.Passport
        User.Type.InternationalPassport.toString() -> User.Type.InternationalPassport

        else -> throw IllegalArgumentException()
    }
