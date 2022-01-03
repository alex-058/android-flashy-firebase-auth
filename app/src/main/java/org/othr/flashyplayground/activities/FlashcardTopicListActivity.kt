package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.othr.flashyplayground.R
import org.othr.flashyplayground.adapter.FlashcardTopicAdapter
import org.othr.flashyplayground.databinding.ActivityFlashcardTopicListBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel

class FlashcardTopicListActivity : AppCompatActivity(), FlashcardTopicAdapter.FlashcardTopicListener {

    lateinit var app : MainApp

    private lateinit var binding: ActivityFlashcardTopicListBinding

    lateinit var refreshTopicListIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // support for binding
        binding = ActivityFlashcardTopicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp (demo creation)
        app = application as MainApp

        // Initialize recycler view / adpater
        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewFlashcardTopics.layoutManager = layoutManager
        var tempList = app.flashcards.findAllTopics()
        binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcards.findAllTopics(), this)

        // Toolbar support
        binding.toolbarFlashcardTopics.title = title
        setSupportActionBar(binding.toolbarFlashcardTopics)

        registerTopicRefreshCallback()
    }

    // Bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_topic_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_topic_item -> {
                // launch FlashcardTopicActivity
                val launcherIntent = Intent(this, FlashcardTopicActivity::class.java)
                refreshTopicListIntentLauncher.launch(launcherIntent)
            }
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

    private fun registerTopicRefreshCallback() {
        refreshTopicListIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Toast.makeText(this, "You just came back from an activity to the flashcard topic list view", Toast.LENGTH_SHORT).show()
                // binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcards.findAllTopics(), this)
                binding.recyclerViewFlashcardTopics.adapter?.notifyDataSetChanged()
            }
    }


}