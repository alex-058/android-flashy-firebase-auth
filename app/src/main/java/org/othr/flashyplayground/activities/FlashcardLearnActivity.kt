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
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardLearnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardLearnBinding
    private lateinit var flashcardDeck: ArrayList<FlashcardModel>

    // Launcher
    // private lateinit var flipCardIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardLearnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // event handling when passing in the flashcard stack
        if (intent.hasExtra("learn")) {
            flashcardDeck = intent.extras?.getParcelableArrayList<FlashcardModel>("learn")!!
            flashcardLearning()
        }

        // trigger callback methods

    }

    /**
     * Handles the learning with the provided flashcard deck
     */
    private fun flashcardLearning () {

        // initialize first flashcard with iterator
        var currentPosition = 0
        var currentFlashcard = flashcardDeck[currentPosition]

        // initialize progress bar
        binding.progressBar.max = flashcardDeck.size
        binding.progressBar.progress = currentPosition+1

        val firstFlashcard = flashcardDeck.first()
        val lastFlahscard = flashcardDeck.last()

        // display initial flashcard
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

            // flip between front and back of flashcard
            when (binding.learnQuestionFront.text) {
                // switch to back
                currentFlashcard.front -> {
                    binding.learnQuestionFront.text = currentFlashcard.back
                    // TODO: Load and show image if present
                    if (currentFlashcard.image != Uri.EMPTY) {
                        Picasso.get()
                            .load(currentFlashcard.image)
                            .into(binding.imageViewFlashcardLearn)
                        binding.imageViewFlashcardLearn.visibility = View.VISIBLE
                    }


                }
                // switch to front
                currentFlashcard.back -> {
                    binding.learnQuestionFront.text = currentFlashcard.front
                    binding.imageViewFlashcardLearn.visibility = View.GONE
                }
            }
        }


    }

}