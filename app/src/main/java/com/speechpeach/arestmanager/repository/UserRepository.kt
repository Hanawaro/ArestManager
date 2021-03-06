package com.speechpeach.arestmanager.repository

import androidx.lifecycle.MutableLiveData
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.servicies.UserResponse
import com.speechpeach.arestmanager.servicies.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val service: UserService) {

    fun get(id: Int) : MutableLiveData<User> {
        val result = MutableLiveData<User>()

        val response = service.get(id)
        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.users?.first()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {  }
        })

        return result
    }

    fun getAll() : MutableLiveData<List<User>> {

        val result = MutableLiveData<List<User>>()

        val response = service.getAll()
        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.users ?: ArrayList()
                } else {
                    result.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                result.value = ArrayList()
            }
        })

        return result
    }

    fun create(user: User): MutableLiveData<Int> {

        val result = MutableLiveData<Int>()

        val response = service.create(
            name = user.name,
            secondName = user.secondName,
            type = user.typeOfDocument,
            number = user.passportNumber,
            set = user.passportSet,
            date = user.dateOfBirth,
            birthplace = user.birthplace
        )

        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.success ?: 0
                } else {
                    result.value = 0
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                result.value = 0
            }
        })

        return result
    }

    fun update(user: User): MutableLiveData<Boolean> {

        val result = MutableLiveData<Boolean>()

        val response = service.update(
            id = user.id,
            name = user.name,
            secondName = user.secondName,
            type = user.typeOfDocument,
            number = user.passportNumber,
            set = user.passportSet,
            date = user.dateOfBirth,
            birthplace = user.birthplace
        )

        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                result.value = response.isSuccessful
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                result.value = false
            }
        })

        return result
    }

    fun delete(id: Int) : MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val response = service.remove(id)

        response.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                result.value = response.isSuccessful
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                result.value = false
            }
        })

        return result
    }
}