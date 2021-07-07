package com.speechpeach.arestmanager.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Arest(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    val name: Int,

    @SerializedName("date")
    val date: Long,

    @SerializedName("number")
    val number: String,

    @SerializedName("base")
    val base: String,

    @SerializedName("sum")
    val sum: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("user_id")
    val userID: Int = 0,

    @SerializedName("user_name")
    val userName: String = "",

    @SerializedName("user_second_name")
    val userSecondName: String = ""
) : Parcelable {
    enum class Type {
        Active, Canceled, Completed;

        override fun toString(): String {
            return when(this) {
                Active -> "ACTIVE"
                Canceled -> "CANCELED"
                Completed -> "COMPLETED"
            }
        }
    }

}