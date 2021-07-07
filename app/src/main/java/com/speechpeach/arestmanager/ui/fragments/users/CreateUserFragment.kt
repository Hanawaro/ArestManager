package com.speechpeach.arestmanager.ui.fragments.users

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
import com.speechpeach.arestmanager.databinding.FragmentCreateUserBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.utils.validation.user.UserValidation
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import com.speechpeach.arestmanager.viewmodels.users.LetUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CreateUserFragment : Fragment(R.layout.fragment_create_user) {

    private lateinit var binding: FragmentCreateUserBinding
    private val args: CreateUserFragmentArgs by navArgs()

    private val viewModel: LetUserViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateUserBinding.inflate(inflater, container, false)

        user = args.user
        if (user != null) {
            viewModel.userDateOfBirth.time = Date(user!!.date * 1000)
        }

        binding.apply {
            user = args.user
            fragment = this@CreateUserFragment
            isNewUser = args.user == null
        }

        activityViewModel.hideBottomMenu()

        return binding.root
    }

    fun showArests() {
        val action = CreateUserFragmentDirections.actionCreateUserFragmentToLocalArestsListFragment(args.user!!.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (user != null) {
            val day = viewModel.userDateOfBirth.get(Calendar.DAY_OF_MONTH)
            val month = viewModel.userDateOfBirth.get(Calendar.MONTH)
            val year = viewModel.userDateOfBirth.get(Calendar.YEAR)
            binding.dateTextField.setText("${day}/${month + 1}/$year")
        }



        val languages = listOf(
            "Passport",
            "International Passport"
        )

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_type, languages)
        binding.spinnerType.adapter = arrayAdapter

        if (user != null) {
            binding.spinnerType.setSelection(languages.indexOf(
                    when (user!!.type) {
                        User.Type.Passport.toString() -> "Passport"
                        else -> "International Passport"
                    }
            ))
        }

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    "Passport" -> {
                        binding.numberTextField.filters = arrayOf( InputFilter.LengthFilter(6) )
                        binding.setTextField.filters = arrayOf( InputFilter.LengthFilter(4) )

                        if (binding.setTextField.text.toString().length != 4) {
                            binding.numberTextField.text?.clear()
                            binding.setTextField.text?.clear()
                        }
                    }
                    "International Passport" -> {
                        binding.numberTextField.filters = arrayOf( InputFilter.LengthFilter(6) )
                        binding.setTextField.filters = arrayOf( InputFilter.LengthFilter(2) )

                        if (binding.setTextField.text.toString().length != 2) {
                            binding.numberTextField.text?.clear()
                            binding.setTextField.text?.clear()
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        binding.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.userDateOfBirth.set(Calendar.YEAR, year)
                    viewModel.userDateOfBirth.set(Calendar.MONTH, month)
                    viewModel.userDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.dateTextField.text?.clear()
                    binding.dateTextField.setText("$dayOfMonth/${month + 1}/$year")
                    binding.dateTextField.error = null
                }
                setOnDismissListener {
                    binding.dateField.clearFocus()
                }


                val day = viewModel.userDateOfBirth.get(Calendar.DAY_OF_MONTH)
                val month = viewModel.userDateOfBirth.get(Calendar.MONTH)
                val year = viewModel.userDateOfBirth.get(Calendar.YEAR)

                datePicker.updateDate(year, month, day)

                datePicker.maxDate = Calendar.getInstance().apply {
                    time = Date()
                    add(Calendar.YEAR, -18)
                }.time.time

                show()
            }
        }

        binding.submitButton.setOnClickListener {

            if (validate()) {

                if (args.user != null) {
                    viewModel.updateUser(user!!.copy(
                        id = args.user!!.id
                    ))
                } else {
                    viewModel.createUser(user!!)
                }

                hideKeyboard()
                findNavController().navigateUp()
            }
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
                user = User(
                    name = binding.nameTextField.text.toString(),
                    secondName = binding.secondTextNameField.text.toString(),
                    type = when (binding.spinnerType.getItemAtPosition(binding.spinnerType.selectedItemPosition)
                        .toString()) {
                        "Passport" -> User.Type.Passport.toString()
                        else -> User.Type.InternationalPassport.toString()
                    },
                    number = Integer.valueOf(binding.numberTextField.text.toString()),
                    set = Integer.valueOf(binding.setTextField.text.toString()),
                    date = viewModel.userDateOfBirth.time.time / 1000,
                    birthplace = binding.birthplaceTextField.text.toString()
                )

                UserValidation.validate(user!!)
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
}