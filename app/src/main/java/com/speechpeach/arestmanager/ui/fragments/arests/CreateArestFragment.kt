package com.speechpeach.arestmanager.ui.fragments.arests

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.speechpeach.arestmanager.R
import com.speechpeach.arestmanager.databinding.FragmentCreateArestBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.ValueConstants
import com.speechpeach.arestmanager.utils.hideKeyboard
import com.speechpeach.arestmanager.viewmodels.arests.CreateArestViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CreateArestFragment: Fragment(R.layout.fragment_create_arest) {

    private lateinit var binding: FragmentCreateArestBinding
    private val args: CreateArestFragmentArgs by navArgs()

    private val viewModel: CreateArestViewModel by viewModels()

    private lateinit var arest: Arest

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateArestBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@CreateArestFragment
            viewmodel = viewModel
            status = "Active"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        initDatePicker()
    }

    fun onSubmit() {
        if (validate()) {
            hideKeyboard()

            if (args.isNewUser) {
                viewModel.createArestAndUser(arest, args.user)
            } else {
                viewModel.createArestByUser(arest, args.user)
            }
            val action = CreateArestFragmentDirections.actionCreateArestFragmentToArestsListFragment()
            findNavController().navigate(action)
        }
    }

    private fun validate(): Boolean {
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
            arest = Arest(
                    name = viewModel.organization,
                    date = viewModel.dateOfRegistration.time.time / 1000,
                    number = binding.numberTextField.text.toString(),
                    base = binding.baseTextField.text.toString(),
                    sum = Integer.valueOf(binding.sumTextField.text.toString()),
                    status = Arest.Type.Active.toString()
            )
        }

        return result
    }

    private fun initSpinner() {
        binding.spinnerType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ArrayList(ValueConstants.Organization.codes.values)
        )

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    ValueConstants.Organization.codes[ValueConstants.Organization.FSSP] -> {
                        viewModel.organization = ValueConstants.Organization.FSSP
                        viewModel.changePassport(ValueConstants.Organization.FSSP, args.user)
                    }
                    ValueConstants.Organization.codes[ValueConstants.Organization.FNS] -> {
                        viewModel.organization = ValueConstants.Organization.FNS
                        viewModel.changePassport(ValueConstants.Organization.FNS, args.user)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }
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