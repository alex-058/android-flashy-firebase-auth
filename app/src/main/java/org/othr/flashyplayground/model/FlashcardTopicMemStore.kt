package org.othr.flashyplayground.model

/**
 * Holds the flashcard topics for the recycler view
 */
class FlashcardTopicMemStore {

    /**
     * This should be displayed in the recycler view
     */
    var flashcardTopics = arrayListOf<FlashcardTopicModel>()

    fun addTopic (flashcardTopic: FlashcardTopicModel) {
        flashcardTopics.add(flashcardTopic)
        }

    fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        return flashcardTopics
    }

}