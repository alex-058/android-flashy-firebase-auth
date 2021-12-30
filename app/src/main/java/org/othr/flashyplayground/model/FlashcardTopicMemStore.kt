package org.othr.flashyplayground.model

/**
 * Holds the flashcard topics for the recycler view
 */
class FlashcardTopicMemStore {

    var flashcardTopics = arrayListOf<FlashcardTopicModel>()

    fun createTopic (flashcardTopic: FlashcardTopicModel) {
        flashcardTopics.add(flashcardTopic)
        }

    fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        return flashcardTopics
    }
}