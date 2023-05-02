package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentBalanceBinding
import hr.sonsanddaughters.equisite.framework.getUserBalance
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.framework.showToast
import hr.sonsanddaughters.equisite.framework.updateBalance
import hr.sonsanddaughters.equisite.util.FirebaseUtil

class BalanceFragment : Fragment() {

    private lateinit var binding: FragmentBalanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)

        updateBalance()
        setTheBalance()
        setIconClickListeners()
        return binding.root
    }

    private fun updateBalance() = FirebaseUtil.db.updateBalance(FirebaseUtil.auth.currentUser!!.uid)

    private fun setTheBalance() {
        FirebaseUtil.db.getUserBalance(FirebaseUtil.auth.currentUser!!.uid).addOnSuccessListener { balance ->
            binding.textViewYourBalance.text = getString(R.string.balance_state, balance.toString())
        }.addOnFailureListener { exception ->
            activity?.showToast(exception.message.toString())
        }
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

}