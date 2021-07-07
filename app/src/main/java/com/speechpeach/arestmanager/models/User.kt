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
    val type: String,

    @SerializedName("number")
    val number: Int,

    @SerializedName("set")
    val set: Int,

    @SerializedName("date")
    val date: Long,

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
