package com.speechpeach.arestmanager.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Arest(
    @SerializedName("arest_id")
    var id: Int = 0,

    @SerializedName("arest_organization_id")
    val organizationID: Int,

    @SerializedName("arest_registration_date")
    val registrationDate: Long,

    @SerializedName("arest_document_number")
    val documentNumber: String,

    @SerializedName("arest_base")
    val base: String,

    @SerializedName("arest_sum")
    val sum: Int,

    @SerializedName("arest_status")
    val status: String,

    @SerializedName("user_id")
    val userID: Int = 0,

    @SerializedName("info_user_name")
    val userName: String = "",

    @SerializedName("info_user_second_name")
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