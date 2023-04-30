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


/**
 * A simple [Fragment] subclass.
 * Use the [InvestmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            Toast.makeText(activity, "Equisite pigeon has been sent. He will return with the message soon!", Toast.LENGTH_LONG).show()
        }
    }
}