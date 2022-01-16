package org.othr.flashyplayground.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    // Hamburger sign of navigation draw
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.root, binding.toolbarSplashScreen, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "You have selected home", Toast.LENGTH_LONG).show()
                }

                R.id.nav_add ->  {
                    Toast.makeText(this, "Clicked Add", Toast.LENGTH_LONG).show()
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
}