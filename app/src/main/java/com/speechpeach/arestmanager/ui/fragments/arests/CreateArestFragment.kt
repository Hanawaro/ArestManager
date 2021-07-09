package com.speechpeach.arestmanager.ui.fragments.arests

import android.annotation.SuppressLint
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
import com.speechpeach.arestmanager.utils.*
import com.speechpeach.arestmanager.viewmodels.arests.CreateArestViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CreateArestFragment: Fragment(R.layout.fragment_create_arest) {

    private lateinit var binding: FragmentCreateArestBinding
    private val args: CreateArestFragmentArgs by navArgs()

    private val viewModel: CreateArestViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateArestBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@CreateArestFragment
            viewmodel = viewModel
            status = ValueConstants.ArestStatus.active
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
                viewModel.createUser(args.user).observe(viewLifecycleOwner) { id ->
                    viewModel.createArest(viewModel.arest, id).observe(viewLifecycleOwner) {
                        val action = CreateArestFragmentDirections.actionCreateArestFragmentToArestsListFragment()
                        findNavController().navigate(action)
                    }
                }
            } else {
                viewModel.createArest(viewModel.arest, args.user.id).observe(viewLifecycleOwner) {
                    val action = CreateArestFragmentDirections.actionCreateArestFragmentToArestsListFragment()
                    findNavController().navigate(action)
                }
            }
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
            viewModel.arest = Arest(
                    organizationID = viewModel.organizationID,
                    registrationDate = viewModel.registrationDate.time.time / 1000,
                    documentNumber = binding.numberTextField.text.toString(),
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
                        viewModel.organizationID = ValueConstants.Organization.FSSP
                        viewModel.formatPassport(ValueConstants.Organization.FSSP, args.user)
                    }
                    ValueConstants.Organization.codes[ValueConstants.Organization.FNS] -> {
                        viewModel.organizationID = ValueConstants.Organization.FNS
                        viewModel.formatPassport(ValueConstants.Organization.FNS, args.user)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {

        viewModel.registrationDate.apply {
            binding.dateTextField.setText("${day()}/${month()}/${year()}")
        }

        binding.dateTextField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                return@setOnFocusChangeListener

            hideKeyboard()

            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->

                    viewModel.registrationDate.apply {
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

                viewModel.registrationDate.apply {
                    datePicker.updateDate(year(), calendarMonth(), day())
                }

                datePicker.maxDate = QuickCalendar.get().time.time

                show()
            }
        }
    }

}