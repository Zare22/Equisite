package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentBalanceBinding
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.framework.showToast
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
            activity?.showToast(getString(R.string.equisite_pigeons_are_working))
        }
        binding.btnScan.setOnClickListener {
            activity?.showToast(getString(R.string.equisite_pigeons_are_working))
        }
        binding.btnUpload.setOnClickListener {
            activity?.showToast(getString(R.string.equisite_pigeons_are_working))
        }
        binding.btnManual.setOnClickListener {
            activity?.replaceFragment(R.id.balanceContainer, ManualFragment())
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
                activity?.showToast(exception.message.toString())
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
                activity?.showToast(exception.message.toString())
            }


        Tasks.whenAll(incomesTask, expensesTask).addOnCompleteListener {
            binding.textViewYourBalance.text = getString(R.string.balance_state, sum.toString())
        }

    }

}