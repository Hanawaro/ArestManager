package com.speechpeach.arestmanager.viewmodels.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.repository.ArestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserArestsListViewModel @Inject constructor(
        private val repository: ArestRepository
): ViewModel() {

    fun getArests(userID: Int): LiveData<List<Arest>> {
        return repository.getUserArests(userID)
    }

}