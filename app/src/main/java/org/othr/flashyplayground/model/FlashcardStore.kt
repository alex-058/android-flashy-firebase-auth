package org.othr.flashyplayground.model

interface FlashcardStore {

    fun create(flashcard: FlashcardModel)
    fun findAll(): ArrayList<FlashcardModel>
    fun update(flashcard: FlashcardModel)

}