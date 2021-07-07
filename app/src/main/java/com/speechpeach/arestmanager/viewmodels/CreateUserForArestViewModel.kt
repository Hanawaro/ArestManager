package com.speechpeach.arestmanager.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateUserForArestViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    val chooseUserValue = "Choose user"
    val createUserValue = "Create user"
    val typeValues  = listOf(chooseUserValue, createUserValue)

    val passportValue = "Passport"
    val internationalPassportValue = "International Passport"
    val passportValues  = listOf(passportValue, internationalPassportValue)

    val users: LiveData<List<User>>
        get() = repository.getAll()

    val userDateOfBirth: Calendar = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.YEAR, -18)
    }

    private val _isUsersHidden = MutableLiveData(false)
    val isUsersHidden: LiveData<Boolean> get() = _isUsersHidden

    fun hideUsers() {
        _isUsersHidden.value = true
    }

    fun showUsers() {
        _isUsersHidden.value = false
    }

}