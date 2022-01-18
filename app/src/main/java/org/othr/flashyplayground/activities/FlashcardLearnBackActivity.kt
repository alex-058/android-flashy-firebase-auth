package org.othr.flashyplayground.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBackBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardLearnBackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFlashcardLearnBackBinding
    lateinit var flashcard: FlashcardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardLearnBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get current flashcard from intent
        if (intent.hasExtra("current_flashcard")) {
            flashcard = intent.getParcelableExtra<FlashcardModel>("current_flashcard")!!
        }

        // Display flashcard answer
        binding.learnQuestionBack.text = flashcard.back

        if (flashcard.image != Uri.EMPTY) {
            Picasso.get()
                .load(flashcard.image)
                .into(binding.imageViewFlashcardBack)
        }

        binding.btnEasy.setOnClickListener {
            finish()
        }
    }
}