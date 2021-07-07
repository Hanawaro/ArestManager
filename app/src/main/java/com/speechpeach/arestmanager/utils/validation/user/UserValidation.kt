package com.speechpeach.arestmanager.utils.validation.user

import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.toUserPassportType

object UserValidation {

    class InvalidNumberException: Exception("Invalid number")
    class InvalidSetException: Exception("Invalid set")
    
    fun validate(user: User) {

        when(user.type.toUserPassportType()) {

            User.Type.Passport -> {
                if (user.number.toString().length != 6)
                    throw InvalidNumberException()
                if (user.set.toString().length != 4)
                    throw InvalidSetException()
            }

            User.Type.InternationalPassport -> {
                if (user.number.toString().length != 6)
                    throw InvalidNumberException()
                if (user.set.toString().length != 2)
                    throw InvalidSetException()
            }
        }

    }

}