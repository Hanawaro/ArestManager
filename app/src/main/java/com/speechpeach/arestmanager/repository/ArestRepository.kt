package com.speechpeach.arestmanager.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.servicies.ArestResponse
import com.speechpeach.arestmanager.servicies.ArestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArestRepository @Inject constructor(private val service: ArestService) : Callback<ArestResponse> {

    fun get(id: Int) : MutableLiveData<Arest> {
        val result = MutableLiveData<Arest>()

        val response = service.get(id)
        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.arests?.first()
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {  }
        })

        return result
    }

    fun getAll() : MutableLiveData<List<Arest>> {

        val result = MutableLiveData<List<Arest>>()

        val response = service.getAll()
        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.arests ?: ArrayList()

                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {

            }
        })

        return result
    }

    fun create(arest: Arest): MutableLiveData<Int> {

        val result = MutableLiveData<Int>()

        val response = service.create(
            name = arest.name,
            date = arest.date,
            number = arest.number,
            base = arest.base,
            sum = arest.sum,
            status = arest.status,
            userID = arest.userID
        )

        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.success ?: 0
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {  }
        })

        return result
    }

    fun create(arest: Arest, user: User): MutableLiveData<Int> {
        val result = MutableLiveData<Int>()

        val response = service.create(
                arestName = arest.name,
                arestDate = arest.date,
                arestNumber = arest.number,
                arestBase = arest.base,
                arestSum = arest.sum,
                arestStatus = arest.status,

                userName = user.name,
                userSecondName = user.secondName,
                userType = user.type,
                userNumber = user.number,
                userSet = user.set,
                userDate = user.date,
                userBirthplace = user.birthplace
        )

        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.success ?: 0
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {  }
        })

        return result
    }


    fun update(arest: Arest) {
        val response = service.update(
            id = arest.id,
            name = arest.name,
            date = arest.date,
            number = arest.number,
            base = arest.base,
            sum = arest.sum,
            status = arest.status,
            userID = arest.userID
        )

        response.enqueue(this)
    }

    fun delete(id: Int) {
        val response = service.remove(id)
        response.enqueue(this)
    }


    override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {  }
    override fun onFailure(call: Call<ArestResponse>, t: Throwable) {  }
}