package com.speechpeach.arestmanager.utils.validation.user

import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.toUserDocumentType

object UserValidation {

    class InvalidNumberException: Exception("Invalid number")
    class InvalidSetException: Exception("Invalid set")
    
    fun validate(user: User) {

        when(user.typeOfDocument.toUserDocumentType()) {

            User.Type.Passport -> {
                if (user.passportNumber.toString().length != 6)
                    throw InvalidNumberException()
                if (user.passportSet.toString().length != 4)
                    throw InvalidSetException()
            }

            User.Type.InternationalPassport -> {
                if (user.passportNumber.toString().length != 6)
                    throw InvalidNumberException()
                if (user.passportSet.toString().length != 2)
                    throw InvalidSetException()
            }
        }

    }

}