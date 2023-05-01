package hr.sonsanddaughters.equisite.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentInvestmentsBinding
import hr.sonsanddaughters.equisite.databinding.FragmentLoginBinding

class InvestmentsFragment : Fragment() {
    private lateinit var binding: FragmentInvestmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvestmentsBinding.inflate(inflater, container, false)
        binding.btnSearchInternet.setOnClickListener {
            showAlert()
        }
        return binding.root
    }

    private fun showAlert() {
        activity?.runOnUiThread {
            Toast.makeText(activity, R.string.equisite_pigeon_has_been_sent_he_will_return_with_the_message_soon, Toast.LENGTH_LONG).show()
        }
    }
}