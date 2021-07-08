package com.speechpeach.arestmanager.servicies

import com.speechpeach.arestmanager.models.Arest
import retrofit2.Call
import retrofit2.http.*

interface ArestService {

    @GET("get_arest.php")
    fun get(
        @Query("id") id: Int
    ): Call<ArestResponse>

    @GET("get_arests.php")
    fun getAll(): Call<ArestResponse>

    @GET("get_local_arests.php")
    fun getUserArests(
        @Query("id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("create_arest.php")
    fun create(
        @Field("name") name: Int,
        @Field("date") date: Long,
        @Field("number") number: String,
        @Field("base") base: String,
        @Field("sum") sum: Int,
        @Field("status") status: String,
        @Field("user_id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("create_arest_and_user.php")
    fun create(
            @Field("arest_name") arestName: Int,
            @Field("arest_date") arestDate: Long,
            @Field("arest_number") arestNumber: String,
            @Field("arest_base") arestBase: String,
            @Field("arest_sum") arestSum: Int,
            @Field("arest_status") arestStatus: String,

            @Field("user_name") userName: String,
            @Field("user_second_name") userSecondName: String,
            @Field("user_type") userType: String,
            @Field("user_number") userNumber: Int,
            @Field("user_set") userSet: Int,
            @Field("user_date") userDate: Long,
            @Field("user_birthplace") userBirthplace: String
    ) : Call<ArestResponse>

    @FormUrlEncoded
    @POST("update_arest.php")
    fun update(
        @Field("id") id: Int,
        @Field("name") name: Int,
        @Field("date") date: Long,
        @Field("number") number: String,
        @Field("base") base: String,
        @Field("sum") sum: Int,
        @Field("status") status: String,
        @Field("user_id") userID: Int
    ): Call<ArestResponse>

    @FormUrlEncoded
    @POST("remove_arest.php")
    fun remove(
        @Field("id") id: Int
    ): Call<ArestResponse>

}
data class ArestResponse(
    val success: Int? = null,
    val message: String? = null,
    val arests: List<Arest>? = null
)