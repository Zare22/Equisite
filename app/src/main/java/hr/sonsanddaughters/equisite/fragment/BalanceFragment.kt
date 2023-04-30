package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentBalanceBinding
import hr.sonsanddaughters.equisite.util.FirebaseUtil

/**
 * A simple [Fragment] subclass.
 * Use the [BalanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

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
                    "Pigeons are working overtime to deliver the feature",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnScan.setOnClickListener {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Pigeons are working overtime to deliver the feature",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnUpload.setOnClickListener {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Pigeons are working overtime to deliver the feature",
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
            binding.textViewYourBalance.text = "Your current balance is: ${sum.toString()}"
        }

    }

}