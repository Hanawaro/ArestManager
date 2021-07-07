package com.speechpeach.arestmanager.servicies

import com.speechpeach.arestmanager.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("get_user.php")
    fun get(
        @Query("id") id: Int
    ): Call<UserResponse>

    @GET("get_users.php")
    fun getAll(): Call<UserResponse>

    @FormUrlEncoded
    @POST("create_user.php")
    fun create(
        @Field("name") name: String,
        @Field("second_name") secondName: String,
        @Field("type") type: String,
        @Field("number") number: Int,
        @Field("set") set: Int,
        @Field("date") date: Long,
        @Field("birthplace") birthplace: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("update_user.php")
    fun update(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("second_name") secondName: String,
        @Field("type") type: String,
        @Field("number") number: Int,
        @Field("set") set: Int,
        @Field("date") date: Long,
        @Field("birthplace") birthplace: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("remove_user.php")
    fun remove(
        @Field("id") id: Int
    ): Call<UserResponse>

}
data class UserResponse(
    val success: Int? = null,
    val message: String? = null,
    val users: ArrayList<User>? = null
)
