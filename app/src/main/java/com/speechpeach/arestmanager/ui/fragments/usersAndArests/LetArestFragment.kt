package com.speechpeach.arestmanager.ui.fragments.usersAndArests

import android.annotation.SuppressLint
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
import com.speechpeach.arestmanager.databinding.FragmentLetArestBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.*
import com.speechpeach.arestmanager.viewmodels.usersAndArests.LetArestViewModel
import com.speechpeach.arestmanager.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LetArestFragment: Fragment(R.layout.fragment_let_arest) {

    private lateinit var binding: FragmentLetArestBinding
    private val args: LetArestFragmentArgs by navArgs()

    private val viewModel: LetArestViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLetArestBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@LetArestFragment
            arest = args.arest
            isEditable = args.isEditable
        }

        activityViewModel.hideBottomMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationDate.time = Date(args.arest.registrationDate * 1000)

        initUserInfo()
        initSpinner()
        initDatePicker()

        viewModel.formattedPassport.observe(viewLifecycleOwner) {
            binding.layoutUser.userPassport.text = it
        }
    }

    fun onSubmit() {
        if (validate()) {
            viewModel.updateArest(viewModel.arest).observe(viewLifecycleOwner) {
                findNavController().navigateUp()
            }
        }
    }

    fun selectUser() {
        if (validate()) {
            val action = LetArestFragmentDirections.actionLetArestFragmentToSelectUserFragment(viewModel.arest)
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
            viewModel.arest = args.arest.copy(
                    organizationID = viewModel.organizationID,
                    registrationDate = viewModel.registrationDate.time.time / 1000,
                    documentNumber = binding.numberTextField.text.toString(),
                    base = binding.baseTextField.text.toString(),
                    sum = Integer.valueOf(binding.sumTextField.text.toString()),
                    status = when(ValueConstants.ArestStatus.values[binding.spinnerStatus.selectedItemPosition]) {
                        ValueConstants.ArestStatus.active -> Arest.Type.Active.toString()
                        ValueConstants.ArestStatus.canceled -> Arest.Type.Canceled.toString()
                        ValueConstants.ArestStatus.completed -> Arest.Type.Completed.toString()

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

            viewModel.user = it

            viewModel.formatPassport(args.arest.organizationID)

            binding.layoutUser.apply {
                val calendar = QuickCalendar.get(viewModel.user!!.dateOfBirth)
                userDate.text = ("${calendar.day()}/${calendar.month()}/${calendar.year()}")
            }
        }
    }

    private fun initSpinner() {

        binding.spinnerType.isEnabled = args.isEditable
        binding.spinnerStatus.isEnabled = args.isEditable

        binding.spinnerType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ArrayList(ValueConstants.Organization.codes.values)
        )

        binding.spinnerType.setSelection(ArrayList(ValueConstants.Organization.codes.values).indexOf( ValueConstants.Organization.codes[args.arest.organizationID] ))

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (parent?.getItemAtPosition(position)?.toString()) {
                    ValueConstants.Organization.codes[ValueConstants.Organization.FSSP] -> {
                        viewModel.organizationID = ValueConstants.Organization.FSSP
                        viewModel.formatPassport(ValueConstants.Organization.FSSP)
                    }
                    ValueConstants.Organization.codes[ValueConstants.Organization.FNS] -> {
                        viewModel.organizationID = ValueConstants.Organization.FNS
                        viewModel.formatPassport(ValueConstants.Organization.FNS)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }
        }

        binding.spinnerStatus.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_type,
                ValueConstants.ArestStatus.values
        )

        binding.spinnerStatus.setSelection(ValueConstants.ArestStatus.values.indexOf(
                when(args.arest.status.toArestStatusType()) {
                    Arest.Type.Active -> ValueConstants.ArestStatus.active
                    Arest.Type.Completed -> ValueConstants.ArestStatus.completed
                    Arest.Type.Canceled -> ValueConstants.ArestStatus.canceled
                }
        ))
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