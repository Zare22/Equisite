package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import hr.sonsanddaughters.equisite.HostActivity
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
        binding.btnBalance.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.balanceFragment)
        }
        binding.btnAnalytics.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.analyticsFragment)
        }
        binding.btnCommunity.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.communityFragment)
        }
        binding.btnInvestments.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.investmentsFragment)
        }
    }


}