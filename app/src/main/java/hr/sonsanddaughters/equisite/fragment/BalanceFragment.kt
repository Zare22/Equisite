package hr.sonsanddaughters.equisite.fragment

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ListenerRegistration
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentBalanceBinding
import hr.sonsanddaughters.equisite.framework.getUserBalance
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.framework.showToast
import hr.sonsanddaughters.equisite.framework.updateBalance
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlin.time.Duration.Companion.seconds

class BalanceFragment : Fragment() {

    private lateinit var binding: FragmentBalanceBinding
    private lateinit var listenerRegistration: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBalanceBinding.inflate(inflater, container, false)

//        val animator = AnimatorInflater.loadAnimator(context, R.animator.number_anim)
//        animator.setTarget(binding.textViewBalanceNumber)
//        animator.start()

        updateBalance()
        setTheBalance()
        setIconClickListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration.remove()
    }

    private fun updateBalance() {
        FirebaseUtil.db.updateBalance(FirebaseUtil.auth.currentUser!!.uid)
    }

    private fun setTheBalance() {
        val listener = { balance: Double ->
            binding.textViewBalanceNumber.text = balance.toString()
        }

        val registration =
            FirebaseUtil.db.getUserBalance(FirebaseUtil.auth.currentUser!!.uid, listener)

        listenerRegistration = registration
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
            activity?.replaceFragment(R.id.balanceFragmentContainer, ManualFragment(), false)
        }
    }

}