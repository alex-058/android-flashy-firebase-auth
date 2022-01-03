package org.othr.flashyplayground.model

interface FlashcardTopicStore {

    fun addTopic(topic: FlashcardTopicModel)
    fun updateTopic(topic: FlashcardTopicModel)
    fun findAllTopics(): ArrayList<FlashcardTopicModel>
    fun setCurrentTopic(topic: FlashcardTopicModel)
}