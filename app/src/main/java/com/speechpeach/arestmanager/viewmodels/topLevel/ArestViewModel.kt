package com.speechpeach.arestmanager.viewmodels.topLevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.repository.ArestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArestViewModel @Inject constructor(
    private val repository: ArestRepository
): ViewModel() {

    fun getArests(): LiveData<List<Arest>> {
        return repository.getAll()
    }

    fun deleteArest(arest: Arest): LiveData<Boolean> {
        return repository.delete(arest.id)
    }

}