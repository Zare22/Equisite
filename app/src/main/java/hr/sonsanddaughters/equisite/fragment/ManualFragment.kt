package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentManualBinding
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.framework.showToast
import hr.sonsanddaughters.equisite.framework.updateBalance
import hr.sonsanddaughters.equisite.model.transaction.Expense
import hr.sonsanddaughters.equisite.model.transaction.Income
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import java.util.Calendar
import java.util.Date


class ManualFragment : Fragment() {

    private lateinit var binding: FragmentManualBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManualBinding.inflate(inflater, container, false)
        binding.rbGroup.setOnCheckedChangeListener { radioGroup, _ ->
            val checkedRb = radioGroup.checkedRadioButtonId
            if (checkedRb == binding.rbIncome.id) {
                val adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.incomes,
                    android.R.layout.simple_spinner_item
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerTypes.adapter = adapter
            } else {
                val adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.expenses,
                    android.R.layout.simple_spinner_item
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerTypes.adapter = adapter
            }
        }
        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.balanceFragment)
        }
        binding.btnSubmit.setOnClickListener {
            addTransactionToUser()
        }

        return binding.root
    }

    private fun addTransactionToUser() {
        val transName = binding.editTextTransactionName.text.toString()
        var transAmount = 0.0
        if (binding.editTextAmount.text.toString().isNotEmpty()) {
            transAmount = binding.editTextAmount.text.toString().toDouble()
        }

        val date = getDate()
        val description = binding.editTextDescription.text.toString()
        var transSubType = ""
        if (binding.spinnerTypes.selectedItem != null) {
            transSubType = binding.spinnerTypes.selectedItem.toString()
        }

        val strings = listOf(transName, description, transSubType)

        if (strings.all { it.isNotEmpty() } && transAmount != 0.0) {
            if (binding.rbIncome.isChecked) {
                val income = Income(
                    transName,
                    transAmount,
                    date,
                    description,
                    FirebaseUtil.auth.currentUser?.uid.toString(),
                    transSubType
                )

                FirebaseUtil.db.collection("incomes").add(income).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        activity?.showToast(it.exception?.message.toString())
                    } else {
                        activity?.showToast(getString(R.string.your_transaction_successfully_flied_south))
                    }
                }
            } else {
                val expense = Expense(
                    transName,
                    transAmount,
                    date,
                    description,
                    FirebaseUtil.auth.currentUser?.uid.toString(),
                    transSubType
                )

                FirebaseUtil.db.collection("expenses").add(expense).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        activity?.showToast(it.exception?.message.toString())
                    } else {
                        activity?.showToast(getString(R.string.your_transaction_successfully_flied_south))
                    }
                }
            }
        } else {
            activity?.showToast(getString(R.string.please_fill_out_your_form))
        }


    }

    private fun getDate(): Date {
        val year = binding.datePicker.year
        val month = binding.datePicker.month
        val day = binding.datePicker.dayOfMonth
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }
}