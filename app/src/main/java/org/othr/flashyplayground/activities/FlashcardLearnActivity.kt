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

    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbarFlashcardLearning)
        supportActionBar?.title = ""

        // Initialization of MainApp
        app = application as MainApp

        flashcardLearning()

        // trigger callback methods
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

        // set flashcard
        var currentFlashcard = app.flashcards.findAllFlashcards()[currentPosition]

        // initialize progress bar
        binding.progressBar.max = app.flashcards.findAllFlashcards().size
        binding.progressBar.progress = currentPosition+1

        // display flashcard
        binding.learnQuestionFront.text = currentFlashcard.front

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

        binding.btnFlipFlashcardFront.setOnClickListener {
            intent = Intent(this, FlashcardLearnBackActivity::class.java)
            intent.putExtra("current_flashcard_position", currentPosition)
            continueLearnIntentLauncher.launch(intent)
        }


    }

    private fun registerLearnIntentLauncher() {
        continueLearnIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Toast.makeText(this, "You just came back from the back of the flashcards", Toast.LENGTH_SHORT).show()
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