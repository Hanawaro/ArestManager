package com.speechpeach.arestmanager.repository

import androidx.lifecycle.MutableLiveData
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.servicies.ArestResponse
import com.speechpeach.arestmanager.servicies.ArestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArestRepository @Inject constructor(private val service: ArestService) {

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
                } else {
                    result.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {
                result.value = ArrayList()
            }
        })

        return result
    }

    fun getUserArests(userID: Int) : MutableLiveData<List<Arest>> {

        val result = MutableLiveData<List<Arest>>()

        val response = service.getUserArests(userID)
        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.arests ?: ArrayList()
                } else {
                    result.value = ArrayList()
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {
                result.value = ArrayList()
            }
        })

        return result
    }

    fun create(arest: Arest): MutableLiveData<Int> {

        val result = MutableLiveData<Int>()

        val response = service.create(
            organizationID = arest.organizationID,
            registrationDate = arest.registrationDate,
            documentNumber = arest.documentNumber,
            base = arest.base,
            sum = arest.sum,
            status = arest.status,
            userID = arest.userID
        )

        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()?.success ?: 0
                } else {
                    result.value = 0
                }
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {
                result.value = 0
            }
        })

        return result
    }

    fun update(arest: Arest): MutableLiveData<Boolean>{

        val result = MutableLiveData<Boolean>()

        val response = service.update(
            id = arest.id,
            organizationID = arest.organizationID,
            registrationDate = arest.registrationDate,
            documentNumber = arest.documentNumber,
            base = arest.base,
            sum = arest.sum,
            status = arest.status,
            userID = arest.userID
        )

        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                result.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {
                result.value = false
            }
        })

        return result
    }

    fun delete(id: Int) : MutableLiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        val response = service.remove(id)
        response.enqueue(object : Callback<ArestResponse> {
            override fun onResponse(call: Call<ArestResponse>, response: Response<ArestResponse>) {
                result.value = response.isSuccessful
            }

            override fun onFailure(call: Call<ArestResponse>, t: Throwable) {
                result.value = false
            }
        })
        return result
    }

}