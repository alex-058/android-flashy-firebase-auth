package org.othr.flashyplayground.model

interface FlashcardStore {

    fun addFlashcard(flashcard: FlashcardModel)
    fun findAllFlashcards(): ArrayList<FlashcardModel>
    fun updateFlashcard( flashcard: FlashcardModel)

}