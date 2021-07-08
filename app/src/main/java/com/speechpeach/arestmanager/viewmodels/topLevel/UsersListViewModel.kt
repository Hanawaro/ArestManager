package com.speechpeach.arestmanager.viewmodels.topLevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    fun  getUsers(): LiveData<List<User>> {
        return repository.getAll()
    }

    fun deleteUser(user: User) : LiveData<Boolean> {
        return repository.delete(user.id)
    }

}