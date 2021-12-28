package org.othr.flashyplayground.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnBackBinding
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnFrontBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardLearnBackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFlashcardLearnBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardLearnBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("learn_back")) {
            val flashcard: FlashcardModel = intent.extras?.getParcelable("learn_back")!!
            binding.learnQuestionBack.text = flashcard.back
        }
    }
}