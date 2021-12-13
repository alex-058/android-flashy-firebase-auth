package org.othr.flashyplayground.model

class FlashcardMemStore: FlashcardStore {

    var flashcards = ArrayList<FlashcardModel>()

    override fun create(flashcard: FlashcardModel) {
        flashcards.add(flashcard)
    }

    override fun findAll(): ArrayList<FlashcardModel> {
        return flashcards
    }

}