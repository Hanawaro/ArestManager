package com.speechpeach.arestmanager.ui.fragments.users

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
import androidx.navigation.fragment.navArgs
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentLetUserBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.*
import com.speechpeach.arestmanager.utils.validation.user.UserValidation
import com.speechpeach.arestmanager.utils.view.*
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import com.speechpeach.arestmanager.viewmodels.users.LetUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LetUserFragment : Fragment(R.layout.fragment_let_user) {

    private lateinit var binding: FragmentLetUserBinding
    private val args: LetUserFragmentArgs by navArgs()

    private val viewModel: LetUserViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLetUserBinding.inflate(inflater, container, false)

        if (args.user != null) {
            viewModel.user = args.user!!
            viewModel.userDateOfBirth.time = Date(viewModel.user.dateOfBirth * 1000)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            user = args.user
            fragment = this@LetUserFragment
            isNewUser = (args.user == null)
        }

        activityViewModel.hideBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        initDatePicker()
    }

    fun showArests() {
        val action = LetUserFragmentDirections.actionLetUserFragmentToUserArestsListFragment(args.user!!.id)
        findNavController().navigate(action)
    }

    fun submit() {
        if (validate()) {

            if (args.user != null) {
                viewModel.updateUser(viewModel.user.copy( id = args.user!!.id )).observe(viewLifecycleOwner) {
                    findNavController().navigateUp()
                }
            } else {
                viewModel.createUser(viewModel.user).observe(viewLifecycleOwner) {
                    findNavController().navigateUp()
                }
            }

            hideKeyboard()
        }
    }

    private fun validate() : Boolean {
        var result = true

        binding.nameTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the name"
                result = false
            }
        }

        binding.secondTextNameField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the second name"
                result = false
            }
        }

        binding.numberTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the number"
                result = false
            }
        }

        binding.setTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the set"
                result = false
            }
        }

        binding.dateTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the date"
                result = false
            }
        }

        binding.birthplaceTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the birthplace"
                result = false
            }
        }

        try {
            if (result) {
                viewModel.user = User(
                    name = binding.nameTextField.text.toString(),
                    secondName = binding.secondTextNameField.text.toString(),
                    typeOfDocument = when (binding.spinnerType.getItemAtPosition(binding.spinnerType.selectedItemPosition).toString()) {
                        ValueConstants.UserDocumentType.passport -> User.Type.Passport.toString()
                        ValueConstants.UserDocumentType.internationalPassport -> User.Type.InternationalPassport.toString()
                        else -> ""
                    },
                    passportNumber = Integer.valueOf(binding.numberTextField.text.toString()),
                    passportSet = Integer.valueOf(binding.setTextField.text.toString()),
                    dateOfBirth = viewModel.userDateOfBirth.time.time / 1000,
                    birthplace = binding.birthplaceTextField.text.toString()
                )

                UserValidation.validate(viewModel.user)
            }
        } catch (exception: UserValidation.InvalidNumberException) {
            binding.numberTextField.error = exception.message
            result = false
        } catch (exception: UserValidation.InvalidSetException) {
            binding.setTextField.error = exception.message
            result = false
        }

        return result
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_type, ValueConstants.UserDocumentType.values)
        binding.spinnerType.adapter = arrayAdapter

        if (args.user != null) {
            binding.spinnerType.setSelection(ValueConstants.UserDocumentType.values.indexOf(
                    when (viewModel.user.typeOfDocument.toUserDocumentType()) {
                        User.Type.Passport -> ValueConstants.UserDocumentType.passport
                        User.Type.InternationalPassport -> ValueConstants.UserDocumentType.internationalPassport
                    }
            ))
        }

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        if (args.user != null) {
            viewModel.userDateOfBirth.apply {
                binding.dateTextField.setText("${day()}/${month()}/${year()}")
            }
        }

        binding.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.userDateOfBirth.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        binding.dateTextField.setText("${day()}/${month()}/${year()}")
                    }

                    binding.dateTextField.error = null
                }

                setOnDismissListener {
                    binding.dateField.clearFocus()
                }

                viewModel.userDateOfBirth.apply {
                    datePicker.updateDate(year(), calendarMonth(), day())
                }

                datePicker.maxDate = QuickCalendar.get(dayOffset = 0, monthOffset = 0, yearOffset = -18).time.time

                show()
            }
        }
    }

    private fun resetPassportFields(numberCount: Int, setCount: Int) {
        binding.numberTextField.filters = arrayOf( InputFilter.LengthFilter(numberCount) )
        binding.setTextField.filters = arrayOf( InputFilter.LengthFilter(setCount) )

        if (binding.setTextField.text.toString().length != setCount) {
            binding.numberTextField.text?.clear()
            binding.setTextField.text?.clear()
        }
    }
}