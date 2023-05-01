package hr.sonsanddaughters.equisite.fragment

import android.content.Context
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
import hr.sonsanddaughters.equisite.databinding.FragmentRegisterBinding
import hr.sonsanddaughters.equisite.framework.registerUser
import hr.sonsanddaughters.equisite.model.User
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnCreateAccount.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val userName = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()


            val validationList = listOf(firstName, lastName, email, userName, password)

            val newUser = User(null, firstName, lastName, userName, email)

            if (validationList.all { it.isNotEmpty() }) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        FirebaseUtil.auth.registerUser(requireActivity(), newUser, password).await()
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
    }

    private fun checkLoggedInState() {
        if (FirebaseUtil.auth.currentUser == null) {
            activity?.runOnUiThread {
                Toast.makeText(activity, R.string.oops_something_went_wrong, Toast.LENGTH_LONG).show()
            }
        } else {
            val inputMethodManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

            val sharedPreferences = requireActivity().getSharedPreferences(
                "MyPrefs",
                Context.MODE_PRIVATE
            )
            with(sharedPreferences.edit()) {
                putString("loggedInUser", FirebaseUtil.auth.currentUser!!.email.toString())
                apply()
            }
            (requireActivity() as HostActivity).updateLoggedInUserTextView()

            (activity as AppCompatActivity).supportActionBar?.show()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentsNavController, HomeFragment())
                ?.commit()
        }
    }
}