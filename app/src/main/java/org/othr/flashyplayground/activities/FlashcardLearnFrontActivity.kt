package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnFrontBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardLearnFrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardLearnFrontBinding
    private lateinit var flashcardDeck: ArrayList<FlashcardModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardLearnFrontBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // event handling when passing in the flashcard stack
        if (intent.hasExtra("learn")) {
            flashcardDeck = intent.extras?.getParcelableArrayList<FlashcardModel>("learn")!!
            flashcardLearning()
        }

    }

    /**
     * Handles the dynamic learning with the flashcard deck
     */
    internal fun flashcardLearning () {

        // initialize iterator to iterate over the flashcard stack
        var iterator = flashcardDeck.listIterator()
        var currentFlashcard = iterator.next()

        val firstFlashcard = flashcardDeck.first()
        val lastFlahscard = flashcardDeck.last()

        // display initial flashcard
        binding.learnQuestionFront.text = currentFlashcard.front

        // grey out next / back buttons
        when (currentFlashcard) {
            firstFlashcard -> disable(binding.btnBackFlashcardLearning)
            lastFlahscard -> disable(binding.btnNextFlashcardLearning)
            else -> {
                enable(binding.btnBackFlashcardLearning)
                enable(binding.btnNextFlashcardLearning)
            }
        }

        binding.btnNextFlashcardLearning.setOnClickListener {
            if (iterator.hasNext()) {
                currentFlashcard = iterator.next()
                binding.learnQuestionFront.text = currentFlashcard.front
            }
            else {
                Toast.makeText(applicationContext, R.string.message_endReached, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnBackFlashcardLearning.setOnClickListener {
            if (iterator.hasPrevious()) {
                currentFlashcard = iterator.previous()
                binding.learnQuestionFront.text = currentFlashcard.front

                }
            else {
                Toast.makeText(applicationContext, R.string.message_beginningReached, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnFlipFlashcard.setOnClickListener {
            // TODO: Introduce Activity for result API functionality
            val launcherIntent = Intent(this, FlashcardLearnBackActivity::class.java)
            launcherIntent.putExtra("learn_back", currentFlashcard)
            startActivityForResult(launcherIntent, 0)
        }


    }

    // TODO: Rework this, not working at the moment
    fun disable(button: Button) {
        button.setAlpha(1.0f)
        button.setClickable(false)
    }

    fun enable(button: Button) {
        button.setAlpha(0.4f)
        button.setClickable(true)
    }

}