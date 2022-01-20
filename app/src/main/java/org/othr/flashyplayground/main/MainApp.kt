package org.othr.flashyplayground.main

import android.app.Application
import org.othr.flashyplayground.model.*

class MainApp : Application() {

    lateinit var flashcards: FlashcardTopicStore
    lateinit var currentUser: FlashyUser


    override fun onCreate() {

        super.onCreate()

        // Holds logged-in firebase user
        currentUser = FlashyUser()

        // Storage
        flashcards = FlashcardJSONStore(applicationContext)


    }

}