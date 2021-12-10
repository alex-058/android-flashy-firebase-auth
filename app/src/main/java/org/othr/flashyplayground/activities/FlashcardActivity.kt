package org.othr.flashyplayground.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.othr.flashyplayground.databinding.ActivityFlashcardAddBinding
import org.othr.flashyplayground.model.FlashcardModel
import com.google.android.material.snackbar.Snackbar

class FlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardAddBinding
    var flashcards = ArrayList<FlashcardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var aFlashcard = FlashcardModel()

        binding.btnSave.setOnClickListener {
            aFlashcard.front = binding.flashcardFront.text.toString()
            aFlashcard.back  = binding.flashcardBack.text.toString()

            if (aFlashcard.front.isNotEmpty() && aFlashcard.back.isNotEmpty()) {

                Snackbar
                    .make(it, "Flashcard was added", Snackbar.LENGTH_LONG)
                    .show()

                // Add Flashcard to list
                flashcards.add(aFlashcard.copy())

                // Request focus operations
                binding.flashcardFront.text.clear()
                binding.flashcardBack.text.clear()
                binding.flashcardFront.requestFocus()

            }

            else {
                Snackbar
                    .make(it, "Flashcard not added", Snackbar.LENGTH_LONG)
                    .show()
            }

        }


    }


}