package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentHomeBinding
import hr.sonsanddaughters.equisite.framework.replaceFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setIconClickListeners()
        return binding.root
    }

    private fun setIconClickListeners() {
        binding.btnBalance.setOnClickListener { activity?.replaceFragment(R.id.fragmentsContainer, BalanceFragment(), false) }
        binding.btnAnalytics.setOnClickListener { activity?.replaceFragment(R.id.fragmentsContainer, AnalyticsFragment(), false) }
        binding.btnCommunity.setOnClickListener { activity?.replaceFragment(R.id.fragmentsContainer, CommunityFragment(), false) }
        binding.btnInvestments.setOnClickListener { activity?.replaceFragment(R.id.fragmentsContainer, InvestmentsFragment(), false) }
    }


}