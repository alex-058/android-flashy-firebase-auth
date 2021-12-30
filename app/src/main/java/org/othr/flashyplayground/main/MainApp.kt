package org.othr.flashyplayground.main

import android.app.Application
import org.othr.flashyplayground.model.FlashcardMemStore
import org.othr.flashyplayground.model.FlashcardModel
import org.othr.flashyplayground.model.FlashcardTopicMemStore
import org.othr.flashyplayground.model.FlashcardTopicModel

class MainApp: Application() {

    val flashcards = FlashcardMemStore()
    var flashcardTopics = FlashcardTopicMemStore()

    // Flashcards for testing

    override fun onCreate() {
        super.onCreate()
        // create a topic also here for testing purposes
        // TODO: trigger create etc. for the flashcards in the property of the topic object

        flashcards.create(FlashcardModel("What is 5 * 5?", "25"))
        flashcards.create(FlashcardModel("What is the capital of England?", "Londong"))
        flashcards.create(FlashcardModel("Who was the first cancellor in Germany?", "Konrad Adenauer"))

        val topic1 = FlashcardTopicModel("Math", "Math 1st semester", flashcards.findAll())
        flashcardTopics.createTopic(topic1)

    }

}