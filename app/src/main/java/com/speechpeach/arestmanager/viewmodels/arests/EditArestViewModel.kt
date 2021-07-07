package com.speechpeach.arestmanager.viewmodels.arests

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
class EditArestViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val arestRepository: ArestRepository
): ViewModel() {

    val activeValue = "Active"
    val completeValue = "Complete"
    val canceledValue = "Canceled"
    val statusValues = listOf(activeValue, completeValue, canceledValue)

    private val _passport = MutableLiveData("")
    val passport: LiveData<String> get() = _passport

    var organization = 0

    val dateOfRegistration: Calendar = Calendar.getInstance().apply {
        time = Date()
    }

    fun getUser(arest: Arest): LiveData<User> = userRepository.get(arest.userID)

    fun updateArest(arest: Arest) {
        arestRepository.update(arest)
    }

    fun changePassport(organization: Int, user: User?) {
        if (user == null)
            return

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