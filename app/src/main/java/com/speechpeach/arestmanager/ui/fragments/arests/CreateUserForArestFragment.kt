package com.speechpeach.arestmanager.ui.fragments.arests

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
import com.speechpeach.arestmanager.utils.adapters.UsersAdapter
import com.speechpeach.arestmanager.utils.hideKeyboard
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

    private lateinit var usersAdapter: UsersAdapter
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        initUsersRecycler()
        initSpinner()
        initDatePicker()
    }

    fun onSubmit() {
        if (validate()) {
            hideKeyboard()
            val action = CreateUserForArestFragmentDirections.actionCreateUserForArestFragmentToCreateArestFragment(user!!, true)
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
                user = User(
                        name = binding.layoutCreateUser.nameTextField.text.toString(),
                        secondName = binding.layoutCreateUser.secondTextNameField.text.toString(),
                        type = when (binding.layoutCreateUser.spinnerType.getItemAtPosition(binding.layoutCreateUser.spinnerType.selectedItemPosition).toString()) {
                            "Passport" -> User.Type.Passport.toString()
                            else -> User.Type.InternationalPassport.toString()
                        },
                        number = Integer.valueOf(binding.layoutCreateUser.numberTextField.text.toString()),
                        set = Integer.valueOf(binding.layoutCreateUser.setTextField.text.toString()),
                        date = viewModel.userDateOfBirth.time.time / 1000,
                        birthplace = binding.layoutCreateUser.birthplaceTextField.text.toString()
                )

                UserValidation.validate(user!!)
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

    private fun initDatePicker() {
        binding.layoutCreateUser.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.userDateOfBirth.set(Calendar.YEAR, year)
                    viewModel.userDateOfBirth.set(Calendar.MONTH, month)
                    viewModel.userDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.layoutCreateUser.dateTextField.text?.clear()
                    binding.layoutCreateUser.dateTextField.setText("$dayOfMonth/${month + 1}/$year")
                    binding.layoutCreateUser.dateTextField.error = null
                }
                setOnDismissListener {
                    binding.layoutCreateUser.dateField.clearFocus()
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
    }

    private fun initSpinner() {

        binding.spinnerType.adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_type, viewModel.typeValues)

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    viewModel.chooseUserValue -> viewModel.showUsers()
                    viewModel.createUserValue -> viewModel.hideUsers()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        binding.layoutCreateUser.spinnerType.adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_type, viewModel.passportValues)

        binding.layoutCreateUser.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                binding.layoutCreateUser.numberTextField.text?.clear()
                binding.layoutCreateUser.setTextField.text?.clear()

                when (parent?.getItemAtPosition(position)?.toString()) {
                    viewModel.passportValue -> {
                        binding.layoutCreateUser.numberTextField.filters = arrayOf( InputFilter.LengthFilter(6) )
                        binding.layoutCreateUser.setTextField.filters = arrayOf( InputFilter.LengthFilter(4) )
                    }
                    viewModel.internationalPassportValue -> {
                        binding.layoutCreateUser.numberTextField.filters = arrayOf( InputFilter.LengthFilter(6) )
                        binding.layoutCreateUser.setTextField.filters = arrayOf( InputFilter.LengthFilter(2) )
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }
    }

    private fun initUsersRecycler() {
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

        viewModel.users.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
        }
    }

}