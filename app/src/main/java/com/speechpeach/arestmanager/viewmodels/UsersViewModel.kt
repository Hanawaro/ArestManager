package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    val users: LiveData<List<User>>
        get() = repository.getAll()

    fun deleteUser(user: User) {
        repository.delete(user.id)
    }

}