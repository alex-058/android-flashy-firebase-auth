package org.othr.flashyplayground.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardListBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.adapter.FlashcardAdapter
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardListActivity : AppCompatActivity(), FlashcardAdapter.FlashcardListener {

    private lateinit var binding: ActivityFlashcardListBinding
    lateinit var app: MainApp
    // Alert Dialog Builder to confirm deletion of flashcard
    lateinit var builder: AlertDialog.Builder

    // Launcher
    private lateinit var refreshListIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Support for Binding
        binding = ActivityFlashcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp

        // Builder for yes,no dialog when deleting an item in the swipe event
        builder = AlertDialog.Builder(this@FlashcardListActivity)
        builder.setTitle(getString(R.string.title_confirm_delete))
        builder.setMessage(getString(R.string.message_confirm_delete_flashcard))

        // Initialize recycler view / adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFlashcards.layoutManager = layoutManager
        binding.recyclerViewFlashcards.adapter = FlashcardAdapter(app.flashcards.findAllFlashcards(), this)

        // Setup toolbar for add functionality
        binding.toolbar.title = app.flashcards.getCurrentTopic().title
        setSupportActionBar(binding.toolbar)

        // Event handling for flashcard add button
        binding.flashcardAddBtn.setOnClickListener {
            // Launch FlashcardActivity
            val launcherIntent = Intent(this, FlashcardActivity::class.java)
            refreshListIntentLauncher.launch(launcherIntent)
        }

        // Attach TouchHelper (Swiping Functionality) to recycler view
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewFlashcards)

        // Trigger callback methods
        registerListRefreshCallback()

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
                refreshListIntentLauncher.launch(launcherIntent)
            }
            R.id.home -> {
                val launcherIntent = Intent(this, SplashScreenActivity::class.java)
                refreshListIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // event handling when clicking certain row / item in recycler view (list)
    override fun onFlashcardItemClickLong(flashcard: FlashcardModel) {
        // Toast.makeText(applicationContext, "You just clicked a flashcard", Toast.LENGTH_LONG).show()
        val launcherIntent = Intent(this, FlashcardActivity::class.java)
        launcherIntent.putExtra("edit_flashcard", flashcard)
        refreshListIntentLauncher.launch(launcherIntent)
    }

    /**
     * Item touch helper property to implement swipe feature for deletion of a flashcard
     * If user swipes item of recycler view (flashcard) to the right, flashcard gets deleted
     */

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        // onMove not needed -> default implementation
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        // event handling if user swipes flashcard in recycler view
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // Setup Dialog if deleting a topic
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                app.flashcards.deleteFlashcard(viewHolder.adapterPosition)
                // notify recycler view that item has been removed
                binding.recyclerViewFlashcards.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
            })

            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                binding.recyclerViewFlashcards.adapter?.notifyDataSetChanged()
                dialog.cancel()
            })

            // Show dialog to user
            val alert = builder.create()
            alert.show()
        }

    }

    /**
     * Callback method:
     * Forces Flashcard list View to refresh itself when coming back from an activity
     */

    private fun registerListRefreshCallback() {
        refreshListIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                binding.recyclerViewFlashcards.adapter = FlashcardAdapter(app.flashcards.findAllFlashcards(), this)
                binding.recyclerViewFlashcards.adapter?.notifyDataSetChanged()
            }
    }
}