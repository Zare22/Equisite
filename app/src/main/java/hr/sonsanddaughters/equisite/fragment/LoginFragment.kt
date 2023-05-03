package hr.sonsanddaughters.equisite.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import hr.sonsanddaughters.equisite.HostActivity
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentLoginBinding
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.framework.showToast
import hr.sonsanddaughters.equisite.framework.updateBalance
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.btnCreateAccount.setOnClickListener {
            openRegisterFragment()
        }
        return binding.root
    }

    private fun openRegisterFragment() {
        activity?.replaceFragment(R.id.fragmentsContainer, RegisterFragment(), false)
    }

    private fun loginUser() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    FirebaseUtil.auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    activity?.showToast(e.message.toString())
                }
            }
        }
        else {
            activity?.showToast("Please fill out your form correctly")
        }
    }

    private fun checkLoggedInState() {
        if (FirebaseUtil.auth.currentUser == null) {
            activity?.replaceFragment(R.id.fragmentsContainer, LoginFragment(), false)
        } else {
            FirebaseUtil.db.updateBalance(FirebaseUtil.auth.currentUser!!.uid)
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("loggedInUser", FirebaseUtil.auth.currentUser!!.email.toString())
                apply()
            }

            (requireActivity() as HostActivity).updateLoggedInUserTextView()
            (activity as AppCompatActivity).supportActionBar?.show()
            activity?.replaceFragment(R.id.fragmentsContainer, HomeFragment(), true)
        }
    }
}