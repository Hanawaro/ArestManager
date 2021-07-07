package com.speechpeach.arestmanager.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.speechpeach.arestmanager.databinding.FragmentEditArestBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.RetrofitConstants
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.viewmodels.EditArestViewModel
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditArestFragment: Fragment(R.layout.fragment_edit_arest) {

    private lateinit var binding: FragmentEditArestBinding
    private val args: EditArestFragmentArgs by navArgs()

    private val viewModel: EditArestViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private lateinit var arest: Arest
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditArestBinding.inflate(inflater, container, false)

        binding.apply {
            fragment = this@EditArestFragment
            arest = args.arest
        }

        activityViewModel.hideBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dateOfRegistration.time = Date(args.arest.date * 1000)

        initUserInfo()
        initSpinner()
        initDatePicker()

        viewModel.passport.observe(viewLifecycleOwner) {
            binding.layoutUser.userPassport.text = it
        }
    }

    fun onSubmit() {
        if (validate()) {

            viewModel.updateArest(arest)

            findNavController().navigateUp()
        }
    }

    fun selectUser() {
        if (validate()) {
            val action = EditArestFragmentDirections.actionEditArestFragmentToSelectUserFragment(arest)
            findNavController().navigate(action)
        }
    }

    private fun validate() : Boolean {

        var result = true

        binding.numberTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the number"
                result = false
            }
        }

        binding.baseTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the base"
                result = false
            }
        }

        binding.sumTextField.apply {
            if (text.isNullOrEmpty()) {
                error = "Enter the sum"
                result = false
            }
        }

        if (result) {
            arest = args.arest.copy(
                    name = viewModel.organization,
                    date = viewModel.dateOfRegistration.time.time / 1000,
                    number = binding.numberTextField.text.toString(),
                    base = binding.baseTextField.text.toString(),
                    sum = Integer.valueOf(binding.sumTextField.text.toString()),
                    status = when(viewModel.statusValues[binding.spinnerStatus.selectedItemPosition]) {
                        viewModel.activeValue -> Arest.Type.Active.toString()
                        viewModel.canceledValue -> Arest.Type.Canceled.toString()
                        viewModel.completeValue -> Arest.Type.Completed.toString()
                        else -> ""
                    }
            )
        }

        return result
    }

    private fun initUserInfo() {
        binding.layoutUser.apply {
            userFullName.text = ("${args.arest.userSecondName} ${args.arest.userName}")
        }

        viewModel.getUser(args.arest).observe(viewLifecycleOwner) {

            user = it

            viewModel.changePassport(args.arest.name, user)

            binding.layoutUser.apply {
                val calendar = Calendar.getInstance().apply {
                    time = Date(user!!.date * 1000)
                }
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                userDate.text = ("$day/$month/$year")
            }
        }
    }

    private fun initSpinner() {

        binding.spinnerType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ArrayList(RetrofitConstants.codes.values)
        )

        binding.spinnerType.setSelection(ArrayList(RetrofitConstants.codes.values).indexOf( RetrofitConstants.codes[args.arest.name] ))

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    RetrofitConstants.codes[17] -> {
                        viewModel.organization = 17
                        viewModel.changePassport(17, user)
                    }
                    RetrofitConstants.codes[39] -> {
                        viewModel.organization = 39
                        viewModel.changePassport(39, user)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        binding.spinnerStatus.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                viewModel.statusValues
        )

        binding.spinnerStatus.setSelection(viewModel.statusValues.indexOf(
                when(args.arest.status) {
                    Arest.Type.Active.toString() -> viewModel.activeValue
                    Arest.Type.Completed.toString() -> viewModel.completeValue
                    Arest.Type.Canceled.toString() -> viewModel.canceledValue
                    else -> ""
                }
        ))
    }

    private fun initDatePicker() {

        val currentDay = viewModel.dateOfRegistration.get(Calendar.DAY_OF_MONTH)
        val currentMonth = viewModel.dateOfRegistration.get(Calendar.MONTH)
        val currentYear = viewModel.dateOfRegistration.get(Calendar.YEAR)
        binding.dateTextField.setText("$currentDay/${currentMonth + 1}/$currentYear")

        binding.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.dateOfRegistration.set(Calendar.YEAR, year)
                    viewModel.dateOfRegistration.set(Calendar.MONTH, month)
                    viewModel.dateOfRegistration.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.dateTextField.text?.clear()
                    binding.dateTextField.setText("$dayOfMonth/${month + 1}/$year")
                    binding.dateTextField.error = null
                }
                setOnDismissListener {
                    binding.dateField.clearFocus()
                }


                val day = viewModel.dateOfRegistration.get(Calendar.DAY_OF_MONTH)
                val month = viewModel.dateOfRegistration.get(Calendar.MONTH)
                val year = viewModel.dateOfRegistration.get(Calendar.YEAR)

                datePicker.updateDate(year, month, day)

                datePicker.maxDate = Calendar.getInstance().apply {
                    time = Date()
                }.time.time

                show()
            }
        }
    }

}