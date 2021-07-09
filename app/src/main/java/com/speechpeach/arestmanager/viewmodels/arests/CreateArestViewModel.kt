package com.speechpeach.arestmanager.viewmodels.arests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.repository.ArestRepository
import com.speechpeach.arestmanager.repository.UserRepository
import com.speechpeach.arestmanager.utils.QuickCalendar
import com.speechpeach.arestmanager.utils.ValueConstants
import com.speechpeach.arestmanager.utils.toUserDocumentType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateArestViewModel @Inject constructor(
        private val arestRepository: ArestRepository,
        private val userRepository: UserRepository
): ViewModel() {

    lateinit var arest: Arest

    var organizationID = 0

    val registrationDate: Calendar = QuickCalendar.get()

    private val _formattedPassport = MutableLiveData("")
    val formattedPassport: LiveData<String> get() = _formattedPassport

    fun createArest(arest: Arest, userID: Int): LiveData<Int> {
        val linkedArest = arest.copy(
                userID = userID
        )
        return arestRepository.create(linkedArest)
    }

    fun createUser(user: User) : LiveData<Int> {
        return userRepository.create(user)
    }

    fun formatPassport(organization: Int, user: User) {
        when(organization) {
            ValueConstants.Organization.FSSP -> when(user.typeOfDocument.toUserDocumentType()) {
                User.Type.Passport ->
                    _formattedPassport.value = "${user.passportSet / 100} ${user.passportSet % 100} ${user.passportNumber}"
                User.Type.InternationalPassport ->
                    _formattedPassport.value = "${user.passportSet} ${user.passportNumber}"
            }

            ValueConstants.Organization.FNS -> when(user.typeOfDocument.toUserDocumentType()) {
                User.Type.Passport->
                    _formattedPassport.value = "${user.passportNumber}-${user.passportSet}"
                User.Type.InternationalPassport ->
                    _formattedPassport.value = "${user.passportNumber}.${user.passportSet}"
            }
        }
    }

}