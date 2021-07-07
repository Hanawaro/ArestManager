package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
        private val repository: UserRepository
): ViewModel() {

        val userDateOfBirth: Calendar = Calendar.getInstance().apply {
                time = Date()
                add(Calendar.YEAR, -18)
        }

        fun createUser(user: User) {
                repository.create(user)
        }

        fun updateUser(user: User) {
                repository.update(user)
        }

}