package org.othr.flashyplayground.model

import android.net.Uri
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import com.squareup.picasso.Picasso

var previousId = 0L

internal fun getId (): Long {
    return previousId++
}

class FlashcardMemStore: FlashcardStore {

    var flashcards = ArrayList<FlashcardModel>()

    override fun create(flashcard: FlashcardModel) {
        flashcard.id = getId()
        flashcards.add(flashcard)
        logAll()
    }

    override fun findAll(): ArrayList<FlashcardModel> {
        return flashcards
    }

    override fun update(flashcard: FlashcardModel) {
        // retrieve flashcard from list to directly work on a list reference
        var foundFlashcard = flashcards.find { p -> p.id == flashcard.id}
        if (foundFlashcard != null) {
            foundFlashcard.image = flashcard.image
            foundFlashcard.front = flashcard.front
            foundFlashcard.back = flashcard.back
            logAll()
        }

    }

    private fun logAll() {
        // log info message
        flashcards.forEach { Timber.i (("$it"))}
    }


}