package org.othr.flashyplayground.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBackBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardModel
import org.othr.flashyplayground.model.Level

class FlashcardLearnBackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFlashcardLearnBackBinding

    lateinit var app: MainApp

    lateinit var flashcard: FlashcardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardLearnBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp

        // get current flashcard from intent
        if (intent.hasExtra("current_flashcard_position")) {
            val position = intent.getIntExtra("current_flashcard_position", 1)
            flashcard = app.flashcards.findAllFlashcards()[position]
        }

        // Display flashcard answer
        binding.learnQuestionBack.text = flashcard.back

        if (flashcard.image != Uri.EMPTY) {
            Picasso.get()
                .load(flashcard.image)
                .resize(800, 600)
                .into(binding.imageViewFlashcardBack)
            // if there is an image available, show it
            binding.imageViewFlashcardBack.visibility = View.VISIBLE
        }

        binding.btnEasy.setOnClickListener {
            flashcard.level = Level.EASY
            finish()
        }

        binding.btnGood.setOnClickListener {
            flashcard.level = Level.GOOD
            finish()
        }

        binding.btnHard.setOnClickListener {
            flashcard.level = Level.HARD
            finish()
        }
    }
}