package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.ArestRepository
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArestViewModel @Inject constructor(
    private val repository: ArestRepository
): ViewModel() {

    val arests: LiveData<List<Arest>>
        get() = repository.getAll()

    fun deleteArest(arest: Arest) {
        repository.delete(arest.id)
    }

}