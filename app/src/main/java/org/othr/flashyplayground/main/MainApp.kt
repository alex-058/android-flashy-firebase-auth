package org.othr.flashyplayground.main

import android.app.Application
import org.othr.flashyplayground.model.FlashcardModel

class MainApp: Application() {

    val flashcards = ArrayList<FlashcardModel>()

    // Flashcards for testing

    override fun onCreate() {
        super.onCreate()
        flashcards.add(FlashcardModel("What is 5 * 5?", "25"))
        flashcards.add(FlashcardModel("What is the capital of England?", "Londong"))
        flashcards.add(FlashcardModel("Who was the first cancellor in Germany?", "Konrad Adenauer"))

    }

}