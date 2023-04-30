package hr.sonsanddaughters.equisite

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import hr.sonsanddaughters.equisite.databinding.ActivityHostBinding
import hr.sonsanddaughters.equisite.fragment.*
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
        initiateNavigationView()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.layout.action_bar_custom_view)
    }

    private fun checkLoggedInState() {
        if (FirebaseUtil.auth.currentUser == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("loggedInUser", FirebaseUtil.auth.currentUser!!.email.toString())
                apply()
            }

            updateLoggedInUserTextView()
        }
    }

    private fun initiateNavigationView() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment())
                        .commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.miBalance -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, BalanceFragment())
                        .commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.miAnalytics -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AnalyticsFragment())
                        .commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.miCommunity -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CommunityFragment())
                        .commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.miInvestments -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, InvestmentsFragment())
                        .commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.miLogout -> {
                    FirebaseUtil.auth.signOut()
                    checkLoggedInState()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        remove("loggedInUser")
                        apply()
                    }
                    updateLoggedInUserTextView()
                }
            }
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
            userHeader.text = "Welcome $loggedInUser"
        } else {
            userHeader.text = "Not logged in"
        }

    }
}