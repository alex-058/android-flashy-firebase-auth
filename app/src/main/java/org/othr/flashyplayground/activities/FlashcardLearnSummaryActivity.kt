package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardLearnSummaryBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardModel
import org.othr.flashyplayground.model.Level
import java.util.*
import kotlin.collections.ArrayList

class FlashcardLearnSummaryActivity : AppCompatActivity() {

    lateinit var binding: ActivityFlashcardLearnSummaryBinding
    lateinit var app: MainApp

    var easyCount: Int = 0
    var goodCount: Int = 0
    var hardCount: Int = 0
    var notLearnedCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardLearnSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp

        // Analyze working deck
        flashcardSummary(app.flashcards.findAllFlashcards())

        binding.textNumberEasy.setText(easyCount.toString())
        binding.textNumberGood.setText(goodCount.toString())
        binding.textNumberHard.setText(hardCount.toString())
        binding.textNumberUnknown.setText(notLearnedCount.toString())

        binding.btnFinishLearning.setOnClickListener {
            // TODO: reset progress / level of flashcards in working set
            resetProgress(app.flashcards.findAllFlashcards())
            intent = Intent(this, FlashcardListActivity::class.java)
            startActivity(intent)
        }

        }

    private fun flashcardSummary (flashcards: ArrayList<FlashcardModel>) {

        // analyzing learning activity
        flashcards.forEach {
            when (it.level) {
                Level.EASY -> easyCount++
                Level.GOOD -> goodCount++
                Level.HARD -> hardCount++
                Level.NOT_LEARNED -> notLearnedCount++
            }
        }


    }

    private fun resetProgress (flashcards: ArrayList<FlashcardModel>) {
        flashcards.forEach {
            it.level = Level.NOT_LEARNED
        }
    }
}