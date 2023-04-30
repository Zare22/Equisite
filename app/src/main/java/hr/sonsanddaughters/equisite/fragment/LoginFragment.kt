package hr.sonsanddaughters.equisite.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.sonsanddaughters.equisite.HostActivity
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.databinding.FragmentLoginBinding
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, RegisterFragment())
            ?.commit()
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
                    activity?.runOnUiThread {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (FirebaseUtil.auth.currentUser == null) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment())
                ?.commit()
        } else {
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
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, HomeFragment())
                ?.commit()
        }
    }
}