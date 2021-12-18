package org.othr.flashyplayground.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import org.othr.flashyplayground.databinding.ActivityFlashcardAddBinding
import org.othr.flashyplayground.model.FlashcardModel
import com.google.android.material.snackbar.Snackbar
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.R
import timber.log.Timber

class FlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardAddBinding
    var aFlashcard = FlashcardModel()

    // Declaration of MainApp
    lateinit var app :  MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Timber Tree for Logging support with Timber
        Timber.plant(Timber.DebugTree())

        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initalization of Main App
        app = application as MainApp

        // Initialize edit flag to check on update / add state (default: false -> add mode)
        var edit = false // is this the right position to declare this variable?

        // Toolbar support
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        if (intent.hasExtra("edit_flashcard")) {
            // Retrieve flashcard data
            aFlashcard = intent.extras?.getParcelable("edit_flashcard")!!
            // Set text flashcard text fields in order to allow user to edit text of existing flashcards
            binding.flashcardBack.setText(aFlashcard.back)
            binding.flashcardFront.setText(aFlashcard.front)
            // Set new toolbar title
            binding.btnSave.setText(R.string.btn_changeFlashcard)
            edit = true
        }

        binding.btnSave.setOnClickListener {
            aFlashcard.front = binding.flashcardFront.text.toString()
            aFlashcard.back  = binding.flashcardBack.text.toString()

            if (aFlashcard.front.isNotEmpty() && aFlashcard.back.isNotEmpty()) {

                if (edit == false) {
                    // Add new flashcard to list
                    app.flashcards.create(aFlashcard.copy())
                    Snackbar
                        .make(it, R.string.message_addedFlashcard, Snackbar.LENGTH_LONG)
                        .show()
                }
                else {
                    // Update existing flashcard
                    app.flashcards.update(aFlashcard.copy())
                    Snackbar
                        .make(it, R.string.message_updatedFlashcard, Snackbar.LENGTH_LONG)
                        .show()
                    finish()
                }


                // Request focus operations
                binding.flashcardFront.text.clear()
                binding.flashcardBack.text.clear()
                binding.flashcardFront.requestFocus()
            }

            else {
                Snackbar
                    .make(it, R.string.message_discardedAddFlashcard, Snackbar.LENGTH_LONG)
                    .show()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel_item -> {
                // Launch Flashcard List Activity
                val launcherIntent = Intent(this, FlashcardListActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}