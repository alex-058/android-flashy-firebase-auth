package org.othr.flashyplayground.model

interface FlashcardStore {

    fun addFlashcard(flashcard: FlashcardModel)
    fun findAllFlashcards(): ArrayList<FlashcardModel>
    fun findFlashcardCount(topic: FlashcardTopicModel): Int
    fun updateFlashcard( flashcard: FlashcardModel)

}