package com.speechpeach.arestmanager.servicies

import com.speechpeach.arestmanager.models.Arest
import retrofit2.Call
import retrofit2.http.*

interface ArestService {

    @GET("arests_get.php")
    fun get(
        @Query("arest_id") id: Int
    ): Call<ArestResponse>

    @GET("arests_get_all.php")
    fun getAll(): Call<ArestResponse>

    @GET("arests_users_get.php")
    fun getUserArests(
        @Query("user_id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("arests_create.php")
    fun create(
            @Field("arest_organization_id") organizationID: Int,
            @Field("arest_registration_date") registrationDate: Long,
            @Field("arest_document_number") documentNumber: String,
            @Field("arest_base") base: String,
            @Field("arest_sum") sum: Int,
            @Field("arest_status") status: String,
            @Field("user_id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("arests_update.php")
    fun update(
            @Field("arest_id") id: Int,
            @Field("arest_organization_id") organizationID: Int,
            @Field("arest_registration_date") registrationDate: Long,
            @Field("arest_document_number") documentNumber: String,
            @Field("arest_base") base: String,
            @Field("arest_sum") sum: Int,
            @Field("arest_status") status: String,
            @Field("user_id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("arests_delete.php")
    fun remove(
        @Field("arest_id") id: Int
    ): Call<ArestResponse>

}
data class ArestResponse(
    val success: Int? = null,
    val message: String? = null,
    val arests: List<Arest>? = null
)