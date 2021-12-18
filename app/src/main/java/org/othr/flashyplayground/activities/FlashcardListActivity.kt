package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardListBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.othr.flashyplayground.adapter.FlashcardAdapter
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardListActivity : AppCompatActivity(), FlashcardAdapter.FlashcardListener {

    private lateinit var binding: ActivityFlashcardListBinding
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Support for Binding
        binding = ActivityFlashcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFlashcards.layoutManager = layoutManager
        binding.recyclerViewFlashcards.adapter = FlashcardAdapter(app.flashcards.findAll(), this)

        // Setup toolbar for add functionality (Actionbar)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

    }

    // Get reference to actual menu buttons from menu resource
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // event handling for items in toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_item -> {
                // launch FlashcardActivity
                val launcherIntent = Intent(this, FlashcardActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // event handling when clicking certain row / item in recycler view
    override fun onFlashcardItemClick(flashcard: FlashcardModel) {
        // Toast.makeText(applicationContext, "You just clicked a flashcard", Toast.LENGTH_LONG).show()
        val launcherIntent = Intent(this, FlashcardActivity::class.java)
        launcherIntent.putExtra("edit_flashcard", flashcard)
        startActivityForResult(launcherIntent, 0)
    }

    /**
     * Forces Flashcard list View to refresh itself
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerViewFlashcards.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}