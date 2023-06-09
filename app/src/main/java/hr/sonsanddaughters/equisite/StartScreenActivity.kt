package hr.sonsanddaughters.equisite

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.children
import androidx.core.view.forEach
import hr.sonsanddaughters.equisite.constants.Constants
import hr.sonsanddaughters.equisite.databinding.ActivityStartScreenBinding
import hr.sonsanddaughters.equisite.framework.callDelayed
import hr.sonsanddaughters.equisite.framework.isOnline
import hr.sonsanddaughters.equisite.framework.startActivity

class StartScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        setViewsVisibility(View.INVISIBLE)
        startAnimation()
        redirectToHost()
    }

    private fun setViewsVisibility(visibilityValue: Int) = binding.myLinearLayout.forEach { view -> view.visibility = visibilityValue }

    private fun startAnimation() {

        val animImg = AnimationUtils.loadAnimation(this, R.anim.anim_splash_image)
        val animText = AnimationUtils.loadAnimation(this, R.anim.anim_splash_image)
        animImg.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) { }

            override fun onAnimationEnd(p0: Animation?) {
                binding.myLinearLayout.children.forEachIndexed { index, view ->
                    if (index != 0) view.startAnimation(animText)
                }
                setViewsVisibility(View.VISIBLE)
            }
            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.ivEquisiteLogo.startAnimation(animImg)
    }

    private fun redirectToHost() {
        if (isOnline()) {
            callDelayed(Constants.DELAY_SPLASH) { startActivity<HostActivity>() }
        }
        else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            callDelayed(Constants.DELAY_SPLASH) { finish() }
        }

    }
}