package com.speechpeach.arestmanager.viewmodels.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import com.speechpeach.arestmanager.utils.QuickCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LetUserViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    lateinit var user: User

    val userDateOfBirth = QuickCalendar.get(dayOffset = 0, monthOffset = 0, yearOffset = -18)

    fun createUser(user: User): LiveData<Int> {
        return repository.create(user)
    }

    fun updateUser(user: User): LiveData<Boolean> {
        return repository.update(user)
    }

}