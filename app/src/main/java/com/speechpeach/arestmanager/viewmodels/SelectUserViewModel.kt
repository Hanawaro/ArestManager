package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.ArestRepository
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectUserViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

    val users: LiveData<List<User>>
        get() = repository.getAll()

}