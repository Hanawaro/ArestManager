package com.speechpeach.arestmanager.viewmodels.usersAndArests

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
class LetArestViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val arestRepository: ArestRepository
): ViewModel() {

    lateinit var arest: Arest
    var user: User? = null

    var organizationID = 0

    val registrationDate: Calendar = QuickCalendar.get()

    private val _formattedPassport = MutableLiveData("")
    val formattedPassport: LiveData<String>
        get() = _formattedPassport

    fun getUser(arest: Arest): LiveData<User> {
        return userRepository.get(arest.userID)
    }

    fun updateArest(arest: Arest): LiveData<Boolean> {
        return arestRepository.update(arest)
    }

    fun formatPassport(organization: Int) {

        if (user == null)
            return

        when(organization) {
            ValueConstants.Organization.FSSP -> when(user!!.typeOfDocument.toUserDocumentType()) {
                User.Type.Passport ->
                    _formattedPassport.value = "${user!!.passportSet / 100} ${user!!.passportSet % 100} ${user!!.passportNumber}"

                User.Type.InternationalPassport ->
                    _formattedPassport.value = "${user!!.passportSet} ${user!!.passportNumber}"
            }

            ValueConstants.Organization.FNS -> when(user!!.typeOfDocument.toUserDocumentType()) {
                User.Type.Passport ->
                    _formattedPassport.value = "${user!!.passportNumber}-${user!!.passportSet}"
                User.Type.InternationalPassport ->
                    _formattedPassport.value = "${user!!.passportNumber}.${user!!.passportSet}"
            }
        }
    }
}