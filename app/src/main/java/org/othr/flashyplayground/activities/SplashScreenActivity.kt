package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import org.othr.flashyplayground.R
import org.othr.flashyplayground.adapter.FlashcardTopicAdapter
import org.othr.flashyplayground.databinding.ActivitySplashScreenBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel

class SplashScreenActivity : AppCompatActivity(), FlashcardTopicAdapter.FlashcardTopicListener {

    lateinit var app : MainApp

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
            }

            true
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

    private fun registerTopicRefreshCallback() {
        refreshTopicListIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Toast.makeText(this, "You just came back from an activity to the flashcard topic list view", Toast.LENGTH_SHORT).show()
                // binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcards.findAllTopics(), this)
                binding.recyclerViewFlashcardTopics.adapter?.notifyDataSetChanged()
            }
    }
}