package com.speechpeach.arestmanager.servicies

import com.speechpeach.arestmanager.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("users_get.php")
    fun get(
        @Query("user_id") id: Int
    ): Call<UserResponse>

    @GET("users_get_all.php")
    fun getAll(): Call<UserResponse>

    @FormUrlEncoded
    @POST("users_create.php")
    fun create(
        @Field("user_name") name: String,
        @Field("user_second_name") secondName: String,
        @Field("user_type_of_document") type: String,
        @Field("user_passport_number") number: Int,
        @Field("user_passport_set") set: Int,
        @Field("user_date_of_birth") date: Long,
        @Field("user_birthplace") birthplace: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("users_update.php")
    fun update(
        @Field("user_id") id: Int,
        @Field("user_name") name: String,
        @Field("user_second_name") secondName: String,
        @Field("user_type_of_document") type: String,
        @Field("user_passport_number") number: Int,
        @Field("user_passport_set") set: Int,
        @Field("user_date_of_birth") date: Long,
        @Field("user_birthplace") birthplace: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("users_delete.php")
    fun remove(
        @Field("user_id") id: Int
    ): Call<UserResponse>

}
data class UserResponse(
    val success: Int? = null,
    val message: String? = null,
    val users: ArrayList<User>? = null
)
