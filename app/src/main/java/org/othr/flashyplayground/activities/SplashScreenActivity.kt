package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.othr.flashyplayground.R
import org.othr.flashyplayground.adapter.FlashcardTopicAdapter
import org.othr.flashyplayground.databinding.ActivitySplashScreenBinding
import org.othr.flashyplayground.firebase.LoginActivity
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel
import org.othr.flashyplayground.model.FlashyUser

class SplashScreenActivity : AppCompatActivity(), FlashcardTopicAdapter.FlashcardTopicListener {

    lateinit var app : MainApp
    lateinit var builder: AlertDialog.Builder

    lateinit var refreshTopicListIntentLauncher: ActivityResultLauncher<Intent>

    // Hamburger sign of navigation draw
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding support
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp (demo creation)
        app = application as MainApp

        // Initialize hamburger menu
        toggle = ActionBarDrawerToggle(this, binding.root, binding.toolbarSplashScreen, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSplashScreen.title = resources.getString(R.string.title_splashScreen)

        // Initialize recycler view / adpater
        val layoutManager = GridLayoutManager(applicationContext, 2)
        binding.recyclerViewFlashcardTopics.layoutManager = layoutManager
        binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcards.findAllTopics(), this)

        // event handling navigation view
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                    // Fade out navigation view
                    binding.root.closeDrawer(GravityCompat.START)
                }

                R.id.nav_add ->  {
                    // launch FlashcardTopicActivity
                    val launcherIntent = Intent(this, FlashcardTopicActivity::class.java)
                    refreshTopicListIntentLauncher.launch(launcherIntent)
                }

                R.id.nav_info -> {
                    // Builder for info dialog in "about" nav view item
                    builder = AlertDialog.Builder(this@SplashScreenActivity)
                    builder.setTitle(getString(R.string.message_about_title_app))
                    builder.setMessage(getString(R.string.message_about_copyright))
                    // make dialog happen
                    var alert = builder.create()
                    alert.show()

                }

                R.id.nav_login -> {
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                }

                R.id.nav_logout -> {
                    // sign out current user from Firebase
                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(this, "You were signed out successfully", Toast.LENGTH_LONG).show()

                    // reset current user object
                    app.currentUser = FlashyUser()

                    // re-launch activity to reset login progress
                    intent = Intent(this, SplashScreenActivity::class.java)
                    refreshTopicListIntentLauncher.launch(intent)

                }
            }
            // Close drawer every time a menu item got called
            binding.root.closeDrawer(GravityCompat.START)
            true
        }

        // login / registering event handling
        if (intent.hasExtra("email_id") && intent.hasExtra("user_id")) {
            // Retrieve login / registering data
            app.currentUser.email = intent.extras?.getString("email_id")!!
            app.currentUser.userId = intent.extras?.getString("user_id")!!
            Toast.makeText(this, "Logged-in user: ${app.currentUser.email} with user_id: ${app.currentUser.userId}", Toast.LENGTH_LONG).show()
        }

        // check if user is already logged in when coming back from another activity
        if (app.currentUser.email.isNotEmpty() && app.currentUser.userId.isNotEmpty()) {

            // disable nav_login -> enable nav_logout
            binding.navView.menu.findItem(R.id.nav_login).setVisible(false)
            binding.navView.menu.findItem(R.id.nav_logout).setVisible(true)

            // set new credentials in login navigation view area
            binding.navView.menu.findItem(R.id.nav_emailUser).setVisible(true)
            binding.navView.menu.findItem(R.id.nav_emailUser).title = app.currentUser.email
        }

        registerTopicRefreshCallback()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Event handling when clicking on an item on the flashcard topic recycler view
     */
    override fun onFlashcardTopicClick(flashcardTopic: FlashcardTopicModel) {
        val launcherIntent = Intent(this, FlashcardListActivity::class.java)
        app.flashcards.setCurrentTopic(flashcardTopic)
        startActivity(launcherIntent)
    }

    /**
     * Event handling when long pressing an item in the flashcard topic recycler view -> edit behaviour
     */
    override fun onFlashcardTopicClickLong(flashcardTopic: FlashcardTopicModel) {
        val launcherIntent = Intent(this, FlashcardTopicActivity::class.java)
        launcherIntent.putExtra("edit_topic", flashcardTopic)
        refreshTopicListIntentLauncher.launch(launcherIntent)
    }

    override fun refreshFlashcardCount(topicModel: FlashcardTopicModel): String {
        var flashcardCount = app.flashcards.findFlashcardMap().get(topicModel)?.size!!
        if (flashcardCount != 1) {
            return "${flashcardCount.toString()} items"
        }
        else {
            return "$flashcardCount item"
        }
    }

    /**
     * Prevent app to close if back button is pressed
     */
    override fun onBackPressed() {
        // do nothing in splash screen activity
    }

    private fun registerTopicRefreshCallback() {
        refreshTopicListIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                // Toast.makeText(this, "You just came back from an activity to the flashcard topic list view", Toast.LENGTH_SHORT).show()
                // update recycler view
                binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcards.findAllTopics(), this)
                binding.recyclerViewFlashcardTopics.adapter?.notifyDataSetChanged()
            }
    }
}