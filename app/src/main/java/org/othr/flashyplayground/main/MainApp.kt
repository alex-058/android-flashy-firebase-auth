package org.othr.flashyplayground.main

import android.app.Application
import org.othr.flashyplayground.model.FlashcardMemStore
import org.othr.flashyplayground.model.FlashcardModel

class MainApp: Application() {

    val flashcards = FlashcardMemStore()

    // Flashcards for testing

    override fun onCreate() {
        super.onCreate()
        flashcards.create(FlashcardModel("What is 5 * 5?", "25"))
        flashcards.create(FlashcardModel("What is the capital of England?", "Londong"))
        flashcards.create(FlashcardModel("Who was the first cancellor in Germany?", "Konrad Adenauer"))

    }

}