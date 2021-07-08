package com.speechpeach.arestmanager.ui.fragments.arests

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentCreateUserForArestBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.*
import com.speechpeach.arestmanager.utils.adapters.UsersAdapter
import com.speechpeach.arestmanager.utils.validation.user.UserValidation
import com.speechpeach.arestmanager.viewmodels.arests.CreateUserForArestViewModel
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateUserForArestFragment: Fragment(R.layout.fragment_create_user_for_arest) {

    private lateinit var binding: FragmentCreateUserForArestBinding

    private val viewModel: CreateUserForArestViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateUserForArestBinding.inflate(inflater, container, false)

        activityViewModel.hideBottomMenu()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@CreateUserForArestFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSpinner()
        initDatePicker()
    }

    fun onSubmit() {
        if (validate()) {
            hideKeyboard()
            val action = CreateUserForArestFragmentDirections.actionCreateUserForArestFragmentToCreateArestFragment(viewModel.user, true)
            findNavController().navigate(action)
        }
    }

    private fun validate() : Boolean {
        var result = true

        binding.layoutCreateUser.nameTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the name"
                result = false
            }
        }

        binding.layoutCreateUser.secondTextNameField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the second name"
                result = false
            }
        }

        binding.layoutCreateUser.numberTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the number"
                result = false
            }
        }

        binding.layoutCreateUser.setTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the set"
                result = false
            }
        }

        binding.layoutCreateUser.dateTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the date"
                result = false
            }
        }

        binding.layoutCreateUser.birthplaceTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the birthplace"
                result = false
            }
        }

        try {
            if (result) {
                viewModel.user = User(
                        name = binding.layoutCreateUser.nameTextField.text.toString(),
                        secondName = binding.layoutCreateUser.secondTextNameField.text.toString(),
                        typeOfDocument = when (binding.layoutCreateUser.spinnerType.getItemAtPosition(binding.layoutCreateUser.spinnerType.selectedItemPosition).toString()) {
                            ValueConstants.UserDocumentType.passport -> User.Type.Passport.toString()
                            ValueConstants.UserDocumentType.internationalPassport -> User.Type.InternationalPassport.toString()
                            else -> ""
                        },
                        passportNumber = Integer.valueOf(binding.layoutCreateUser.numberTextField.text.toString()),
                        passportSet = Integer.valueOf(binding.layoutCreateUser.setTextField.text.toString()),
                        dateOfBirth = viewModel.userDateOfBirth.time.time / 1000,
                        birthplace = binding.layoutCreateUser.birthplaceTextField.text.toString()
                )

                UserValidation.validate(viewModel.user)
            }
        } catch (exception: UserValidation.InvalidNumberException) {
            binding.layoutCreateUser.numberTextField.error = exception.message
            result = false
        } catch (exception: UserValidation.InvalidSetException) {
            binding.layoutCreateUser.setTextField.error = exception.message
            result = false
        }

        return result
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        binding.layoutCreateUser.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.userDateOfBirth.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        binding.layoutCreateUser.dateTextField.setText("${day()}/${month()}/${year()}")
                    }
                    binding.layoutCreateUser.dateTextField.error = null
                }
                setOnDismissListener {
                    binding.layoutCreateUser.dateField.clearFocus()
                }

                viewModel.userDateOfBirth.apply {
                    datePicker.updateDate(year(), calendarMonth(), day())
                }

                datePicker.maxDate = QuickCalendar.get(dayOffset = 0, monthOffset = 0, yearOffset = -18).time.time

                show()
            }
        }
    }

    private fun initSpinner() {

        binding.spinnerType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ValueConstants.ArestCreatedMethod.values
        )

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    ValueConstants.ArestCreatedMethod.chooseUser -> viewModel.showUsersList()
                    ValueConstants.ArestCreatedMethod.createUser -> viewModel.hideUsersList()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        binding.layoutCreateUser.spinnerType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ValueConstants.UserDocumentType.values
        )

        binding.layoutCreateUser.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                ValueConstants.UserDocumentType.apply {
                    when (parent?.getItemAtPosition(position)?.toString()) {
                        passport -> {
                            resetPassportFields(
                                    numberCount = countNumberLimit[passport]!!,
                                    setCount = countSetLimit[passport]!!
                            )
                        }
                        internationalPassport -> {
                            resetPassportFields(
                                    numberCount = countNumberLimit[internationalPassport]!!,
                                    setCount = countSetLimit[internationalPassport]!!
                            )
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }
    }

    private fun initAdapter() {
        lateinit var usersAdapter: UsersAdapter

        val onItemClickListener = object : UsersAdapter.ItemClickListener {
            override fun onItemClick(user: User) {
                val action = CreateUserForArestFragmentDirections.actionCreateUserForArestFragmentToCreateArestFragment(user, false)
                findNavController().navigate(action)
            }
            override fun onItemLongClick(user: User): Boolean { return true }
        }

        usersAdapter = UsersAdapter(onItemClickListener)

        binding.layoutUsersList.recyclerUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.getUsers().observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
        }
    }

    private fun resetPassportFields(numberCount: Int, setCount: Int) {
        binding.layoutCreateUser.numberTextField.filters = arrayOf( InputFilter.LengthFilter(numberCount) )
        binding.layoutCreateUser.setTextField.filters = arrayOf( InputFilter.LengthFilter(setCount) )

        if (binding.layoutCreateUser.setTextField.text.toString().length != setCount) {
            binding.layoutCreateUser.numberTextField.text?.clear()
            binding.layoutCreateUser.setTextField.text?.clear()
        }
    }

}