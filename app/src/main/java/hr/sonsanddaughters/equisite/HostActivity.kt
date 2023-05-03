package hr.sonsanddaughters.equisite

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import hr.sonsanddaughters.equisite.databinding.ActivityHostBinding
import hr.sonsanddaughters.equisite.fragment.AnalyticsFragment
import hr.sonsanddaughters.equisite.fragment.BalanceFragment
import hr.sonsanddaughters.equisite.fragment.CommunityFragment
import hr.sonsanddaughters.equisite.fragment.HomeFragment
import hr.sonsanddaughters.equisite.fragment.InvestmentsFragment
import hr.sonsanddaughters.equisite.fragment.LoginFragment
import hr.sonsanddaughters.equisite.framework.replaceFragment
import hr.sonsanddaughters.equisite.util.FirebaseUtil

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out
        )
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        checkLoggedInState()
        initNavigationView()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.layout.action_bar_custom_view)
    }

    private fun checkLoggedInState() {
        if (FirebaseUtil.auth.currentUser == null) {
            this.replaceFragment(binding.fragmentsContainer.id, LoginFragment(), false)
        } else {
            this.replaceFragment(binding.fragmentsContainer.id, HomeFragment(), false)

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("loggedInUser", FirebaseUtil.auth.currentUser!!.email.toString())
                apply()
            }
            updateLoggedInUserTextView()
        }
    }

    private fun initNavigationView() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        setNavigationListeners()
    }

    private fun setNavigationListeners() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> { replaceFragment(R.id.fragmentsContainer, HomeFragment(), true) }
                R.id.miBalance -> { replaceFragment(R.id.fragmentsContainer, BalanceFragment(), false) }
                R.id.miAnalytics -> { replaceFragment(R.id.fragmentsContainer, AnalyticsFragment(), false) }
                R.id.miCommunity -> { replaceFragment(R.id.fragmentsContainer, CommunityFragment(), false) }
                R.id.miInvestments -> { replaceFragment(R.id.fragmentsContainer, InvestmentsFragment(), false) }
                R.id.miLogout -> { showLogoutConfirmationDialog() }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateLoggedInUserTextView() {

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val loggedInUser = sharedPreferences.getString("loggedInUser", "")

        val headerView = binding.navView.getHeaderView(0)
        val userHeader = headerView.findViewById<TextView>(R.id.textViewUserHeader)

        if (loggedInUser != "") {
            userHeader.text = getString(R.string.welcome_message, loggedInUser)
        } else {
            userHeader.setText(R.string.not_logged_in_message)
        }

    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout_title)
        builder.setMessage(R.string.logout_message)

        builder.setPositiveButton(R.string.logout_confirm_button) { _, _ ->
            FirebaseUtil.auth.signOut()
            checkLoggedInState()
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                remove("loggedInUser")
                apply()
            }
            updateLoggedInUserTextView()
        }
        builder.setNegativeButton(R.string.logout_cancel, null)

        val dialog = builder.create()
        dialog.show()
    }
}