package org.othr.flashyplayground.model

interface FlashcardStore {

    fun create(flashcard: FlashcardModel)
    fun findAll(): ArrayList<FlashcardModel>

}