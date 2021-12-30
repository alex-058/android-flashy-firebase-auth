package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardListBinding
import androidx.recyclerview.widget.LinearLayoutManager
import org.othr.flashyplayground.adapter.FlashcardAdapter
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardMemStore
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardListActivity : AppCompatActivity(), FlashcardAdapter.FlashcardListener {

    private lateinit var binding: ActivityFlashcardListBinding
    lateinit var flashcardDeck: FlashcardMemStore
    lateinit var app: MainApp

    // Launcher
    private lateinit var refreshListIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Support for Binding
        binding = ActivityFlashcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp

        // Retrieve flashcards from the topic activity
        if (intent.hasExtra("flashcard_list")) {
            var flashcards: ArrayList<FlashcardModel> = intent.extras?.getParcelableArrayList<FlashcardModel>("flashcard_list")!!
            flashcardDeck.flashcards = flashcards
        }


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFlashcards.layoutManager = layoutManager
        binding.recyclerViewFlashcards.adapter = FlashcardAdapter(flashcardDeck.findAll(), this)

        // Setup toolbar for add functionality (Actionbar)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // Trigger callback methods
        registerRefreshCallback()

    }

    // Get reference to actual menu buttons from menu resource
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // event handling for items in toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start_learn -> {
                // Toast.makeText(applicationContext, "Starting Learn Activity", Toast.LENGTH_LONG).show()
                val launcherIntent = Intent(this, FlashcardLearnActivity::class.java)
                // pass in the flashcards array list (stack) needed for learning activity
                launcherIntent.putParcelableArrayListExtra("learn" , flashcardDeck.findAll())
                refreshListIntentLauncher.launch(launcherIntent)
            }
            R.id.add_item -> {
                // launch FlashcardActivity
                val launcherIntent = Intent(this, FlashcardActivity::class.java)
                refreshListIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // event handling when clicking certain row / item in recycler view (list)
    override fun onFlashcardItemClick(flashcard: FlashcardModel) {
        // Toast.makeText(applicationContext, "You just clicked a flashcard", Toast.LENGTH_LONG).show()
        val launcherIntent = Intent(this, FlashcardActivity::class.java)
        launcherIntent.putExtra("edit_flashcard", flashcard)
        refreshListIntentLauncher.launch(launcherIntent)
    }

    /**
     * Callback method:
     * Forces Flashcard list View to refresh itself when coming back from an activity
     */

    private fun registerRefreshCallback() {
        refreshListIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                // Toast.makeText(this, "You just came back from an activity to the flashcard list view", Toast.LENGTH_SHORT).show()
                binding.recyclerViewFlashcards.adapter?.notifyDataSetChanged()
            }
    }
}