package org.othr.flashyplayground.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import org.othr.flashyplayground.R
import org.othr.flashyplayground.adapter.FlashcardAdapter
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardLearnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardLearnBinding
    private lateinit var flashcardDeck: ArrayList<FlashcardModel>

    private lateinit var continueLearnIntentLauncher: ActivityResultLauncher<Intent>

    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // toolbar support
        // setSupportActionBar(binding.toolbarFlashcardLearning)

        // event handling when passing in the flashcard stack
        if (intent.hasExtra("learn")) {
            // This is the flashcard stack to learn with
            flashcardDeck = intent.extras?.getParcelableArrayList<FlashcardModel>("learn")!!
            // initial flashcard learning
            flashcardLearning()
        }

        // trigger callback methods
        registerLearnIntentLauncher()
    }

    /**
     * Handles the learning with the provided flashcard deck
     */
    private fun flashcardLearning () {

        // set flashcard
        var currentFlashcard = flashcardDeck[currentPosition]

        // initialize progress bar
        binding.progressBar.max = flashcardDeck.size
        binding.progressBar.progress = currentPosition+1

        // display flashcard
        binding.learnQuestionFront.text = currentFlashcard.front

        binding.btnNextFlashcardLearning.setOnClickListener {
            if (currentPosition < flashcardDeck.size - 1) {
                currentPosition++
                currentFlashcard = flashcardDeck[currentPosition]
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
                currentFlashcard = flashcardDeck[currentPosition]
                binding.progressBar.progress = currentPosition + 1
                binding.learnQuestionFront.text = currentFlashcard.front
            }
            else {
                Toast.makeText(applicationContext, R.string.message_beginningReached, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnFlipFlashcardFront.setOnClickListener {
            intent = Intent(this, FlashcardLearnBackActivity::class.java)
            // this is all it has to deal with
            intent.putExtra("current_flashcard", currentFlashcard)
            continueLearnIntentLauncher.launch(intent)
        }


    }

    private fun registerLearnIntentLauncher() {
        continueLearnIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Toast.makeText(this, "You just came back from the back of the flashcards", Toast.LENGTH_SHORT).show()
                if (currentPosition < flashcardDeck.size - 1) {
                    currentPosition++
                }
                flashcardLearning()

            }
    }

}