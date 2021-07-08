package com.speechpeach.arestmanager.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("second_name")
    val secondName: String,

    @SerializedName("type")
    val typeOfDocument: String,

    @SerializedName("number")
    val passportNumber: Int,

    @SerializedName("set")
    val passportSet: Int,

    @SerializedName("date")
    val dateOfBirth: Long,

    @SerializedName("birthplace")
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
