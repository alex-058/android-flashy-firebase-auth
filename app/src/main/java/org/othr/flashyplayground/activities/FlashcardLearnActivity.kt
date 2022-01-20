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
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBinding
import org.othr.flashyplayground.main.MainApp

class FlashcardLearnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardLearnBinding
    lateinit var app: MainApp

    private lateinit var continueLearnIntentLauncher: ActivityResultLauncher<Intent>

    // Holds current position of learning activity
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar setup
        setSupportActionBar(binding.toolbarFlashcardLearning)
        supportActionBar?.title = ""

        // Initialization of MainApp
        app = application as MainApp

        // Start learning
        flashcardLearning()

        // Trigger callback methods
        registerLearnIntentLauncher()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_learn, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // event handling for items in toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.stopLearning -> {
                val launcherIntent = Intent(this, FlashcardLearnSummaryActivity::class.java)
                startActivity(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Handles the learning with the provided flashcard deck
     */
    private fun flashcardLearning () {

        // Set flashcard
        var currentFlashcard = app.flashcards.findAllFlashcards()[currentPosition]

        // Initialize progress bar to display progress in flashcard deck
        binding.progressBar.max = app.flashcards.findAllFlashcards().size
        binding.progressBar.progress = currentPosition+1

        // Display flashcard
        binding.learnQuestionFront.text = currentFlashcard.front

        // Event handling button next
        binding.btnNextFlashcardLearning.setOnClickListener {
            if (currentPosition < app.flashcards.findAllFlashcards().size - 1) {
                currentPosition++
                currentFlashcard = app.flashcards.findAllFlashcards()[currentPosition]
                binding.progressBar.progress = currentPosition + 1
                binding.learnQuestionFront.text = currentFlashcard.front
            }
            else {
                Toast.makeText(applicationContext, R.string.message_endReached, Toast.LENGTH_LONG).show()
            }
        }

        // Event handling button back
        binding.btnBackFlashcardLearning.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                currentFlashcard = app.flashcards.findAllFlashcards()[currentPosition]
                binding.progressBar.progress = currentPosition + 1
                binding.learnQuestionFront.text = currentFlashcard.front
            }
            else {
                Toast.makeText(applicationContext, R.string.message_beginningReached, Toast.LENGTH_LONG).show()
            }
        }

        // Event handling flip flashcard button, launches flashcard back activity and provides current position to get result for particular flashcard
        binding.btnFlipFlashcardFront.setOnClickListener {
            intent = Intent(this, FlashcardLearnBackActivity::class.java)
            intent.putExtra("current_flashcard_position", currentPosition)
            continueLearnIntentLauncher.launch(intent)
        }


    }

    /**
     * Callback method when coming back from flashcard back activity
     */
    private fun registerLearnIntentLauncher() {
        continueLearnIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                // if end is not reached
                if (currentPosition < app.flashcards.findAllFlashcards().size - 1) {
                    currentPosition++
                    flashcardLearning()
                }
                else { // end reached -> send to summary activity
                    intent = Intent(this, FlashcardLearnSummaryActivity::class.java)
                    startActivity(intent)
                }

            }
    }

}