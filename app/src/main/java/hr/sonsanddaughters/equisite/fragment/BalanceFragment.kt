package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Tasks
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentBalanceBinding
import hr.sonsanddaughters.equisite.util.FirebaseUtil

class BalanceFragment : Fragment() {

    private lateinit var binding: FragmentBalanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)
        calculateBalance()
        setIconClickListeners()
        return binding.root
    }

    private fun setIconClickListeners() {
        binding.btnConnectWithBankAccount.setOnClickListener {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    R.string.equisite_pigeons_are_working,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnScan.setOnClickListener {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    R.string.equisite_pigeons_are_working,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnUpload.setOnClickListener {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    R.string.equisite_pigeons_are_working,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnManual.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.balanceContainer, ManualFragment())
                ?.commit()
        }

    }


    private fun calculateBalance() {

        var sum = 0.0
        val incomesTask = FirebaseUtil.db.collection("incomes")
            .whereEqualTo("uid", FirebaseUtil.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    sum += document.getDouble("amount") ?: 0.0
                }
            }
            .addOnFailureListener { exception ->
                activity?.runOnUiThread {
                    Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
                }
            }


        val expensesTask = FirebaseUtil.db.collection("expenses")
            .whereEqualTo("uid", FirebaseUtil.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    sum -= document.getDouble("amount") ?: 0.0
                }
            }
            .addOnFailureListener { exception ->
                activity?.runOnUiThread {
                    Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
                }
            }


        Tasks.whenAll(incomesTask, expensesTask).addOnCompleteListener {
            binding.textViewYourBalance.text = getString(R.string.balance_state, sum.toString())
        }

    }

}