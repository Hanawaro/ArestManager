package com.speechpeach.arestmanager.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("user_id")
    var id: Int = 0,

    @SerializedName("user_name")
    val name: String,

    @SerializedName("user_second_name")
    val secondName: String,

    @SerializedName("user_type_of_document")
    val typeOfDocument: String,

    @SerializedName("user_passport_number")
    val passportNumber: Int,

    @SerializedName("user_passport_set")
    val passportSet: Int,

    @SerializedName("user_date_of_birth")
    val dateOfBirth: Long,

    @SerializedName("user_birthplace")
    val birthplace: String
) : Parcelable {

    enum class Type {
        Passport, InternationalPassport;

        override fun toString(): String {
            return when(this) {
                Passport -> "PASSPORT"
                InternationalPassport -> "INTERNATIONAL PASSPORT"
            }
        }
    }
}
