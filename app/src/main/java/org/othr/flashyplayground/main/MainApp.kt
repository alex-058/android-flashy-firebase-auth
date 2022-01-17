package org.othr.flashyplayground.main

import android.app.Application
import org.othr.flashyplayground.model.*

class MainApp : Application() {

    lateinit var flashcards: FlashcardTopicStore
    lateinit var currentUser: FlashyUser

    // Flashcards for testing

    override fun onCreate() {

        super.onCreate()
        currentUser = FlashyUser()
        flashcards = FlashcardJSONStore(applicationContext)

        /*
        // Setup demo environment
        val topic1 = FlashcardTopicModel(title = "Math", description = "Math 1st semester")
        val topic2 = FlashcardTopicModel(title = "Spanish", description = "Spanish vocs")

        flashcards.addTopic(topic1)

        flashcards.setCurrentTopic(topic1)
        flashcards.addFlashcard(FlashcardModel("What is 5 * 5?", "25"))

        flashcards.setCurrentTopic(topic2)
        flashcards.addFlashcard(FlashcardModel("What is the capital of England?", "London"))
        flashcards.addFlashcard(FlashcardModel("Who was the first cancellor in Germany?", "Konrad Adenauer"))

         */

    }

}