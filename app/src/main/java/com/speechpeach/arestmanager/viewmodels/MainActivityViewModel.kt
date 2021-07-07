package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class MainActivityViewModel: ViewModel() {

    private val _isShowBottomMenu = MutableLiveData(true)
    val isShowBottomMenu: LiveData<Boolean>
        get() = _isShowBottomMenu

    fun hideBottomMenu() {
        if (_isShowBottomMenu.value != false)
            _isShowBottomMenu.value = false
    }

    fun showBottomMenu() {
        if (_isShowBottomMenu.value != true)
            _isShowBottomMenu.value = true
    }
}