package com.speechpeach.arestmanager.viewmodels.arests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import com.speechpeach.arestmanager.utils.QuickCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateUserForArestViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    lateinit var user: User

    fun getUsers(): LiveData<List<User>> {
        return repository.getAll()
    }

    val userDateOfBirth: Calendar = QuickCalendar.get(dayOffset = 0, monthOffset = 0, yearOffset = -18)

    private val _isUsersListHidden = MutableLiveData(false)
    val isUsersListHidden: LiveData<Boolean> get() = _isUsersListHidden

    fun hideUsersList() {
        _isUsersListHidden.value = true
    }

    fun showUsersList() {
        _isUsersListHidden.value = false
    }

}