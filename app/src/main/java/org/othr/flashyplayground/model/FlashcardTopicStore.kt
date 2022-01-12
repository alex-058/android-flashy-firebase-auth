package org.othr.flashyplayground.model

interface FlashcardTopicStore {

    /**
     * Methods for flashcard operations
     */

    fun addFlashcard(flashcard: FlashcardModel)
    fun updateFlashcard( flashcard: FlashcardModel)
    fun findAllFlashcards(): ArrayList<FlashcardModel>
    fun findFlashcardCount(topic: FlashcardTopicModel): Int

    /**
     * Methods for topic operations
     */

    fun addTopic(topic: FlashcardTopicModel)
    fun updateTopic(topic: FlashcardTopicModel)
    fun deleteTopic(topic: FlashcardTopicModel)
    fun findAllTopics(): ArrayList<FlashcardTopicModel>
    fun setCurrentTopic(topic: FlashcardTopicModel)
    fun getCurrentTopic(): FlashcardTopicModel

    /**
     * Methods for map operation
     */

    fun findFlashcardMap(): Map<FlashcardTopicModel, ArrayList<FlashcardModel>>
}