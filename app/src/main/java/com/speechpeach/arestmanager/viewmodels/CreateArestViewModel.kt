package com.speechpeach.arestmanager.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.ArestRepository
import com.speechpeach.arestmanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateArestViewModel @Inject constructor(
        private val repository: ArestRepository
): ViewModel() {

    var organization = 0

    val dateOfRegistration: Calendar = Calendar.getInstance().apply {
        time = Date()
    }

    private val _passport = MutableLiveData("")
    val passport: LiveData<String> get() = _passport

    fun createArestByUser(arest: Arest, user: User) {
        val linkedArest = arest.copy(
                userID = user.id
        )
        repository.create(linkedArest)
    }

    fun createArestAndUser(arest: Arest, user: User) {
        repository.create(arest, user)
    }

    fun changePassport(organization: Int, user: User) {
        when(organization) {
            17 -> when(user.type) {
                User.Type.Passport.toString() ->
                    _passport.value = "${user.set / 100} ${user.set % 100} ${user.number}"

                User.Type.InternationalPassport.toString() ->
                    _passport.value = "${user.set} ${user.number}"
            }

            39 -> when(user.type) {
                User.Type.Passport.toString() ->
                    _passport.value = "${user.number}-${user.set}"
                User.Type.InternationalPassport.toString() ->
                    _passport.value = "${user.number}.${user.set}"
            }
        }
    }

}