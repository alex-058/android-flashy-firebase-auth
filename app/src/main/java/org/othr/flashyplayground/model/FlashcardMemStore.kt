package org.othr.flashyplayground.model

import timber.log.Timber

var previousIdFlashcard = 0L
var previousIdTopic = 0L

internal fun getIdFlashcard (): Long {
    return previousIdFlashcard++
}
internal fun getIdTopic (): Long {
    return previousIdTopic++
}

class FlashcardMemStore: FlashcardTopicStore {

    // Overall collection to store topic (key) and corresponding flashcard deck (value)
    var flashcardMap =  mutableMapOf<FlashcardTopicModel, ArrayList<FlashcardModel>>()

    // stores topic for current working flashcard deck for convenience reasons
    lateinit var workingTopic: FlashcardTopicModel

    /**
     * Flashcard operations
     */

    override fun addFlashcard(flashcard: FlashcardModel) {
        flashcard.id = getIdFlashcard()
        flashcardMap.get(workingTopic)?.add(flashcard)
        logAll()
    }

    override fun findAllFlashcards(): ArrayList<FlashcardModel> {
        // retrieve flashcards from provided topic
        return flashcardMap.getValue(workingTopic)
    }

    override fun findFlashcardCount(topic: FlashcardTopicModel): Int {
        return flashcardMap.getValue(topic).size
    }

    override fun updateFlashcard(flashcard: FlashcardModel) {
        // retrieve flashcard from map value to directly work on the list reference
        var foundFlashcard = flashcardMap.get(workingTopic)?.find { p -> p.id == flashcard.id}
        if (foundFlashcard != null) {
            foundFlashcard.image = flashcard.image
            foundFlashcard.front = flashcard.front
            foundFlashcard.back = flashcard.back
            logAll()
        }

    }

    override fun deleteFlashcard(position: Int) {
        findAllFlashcards().removeAt(position)
        logAll()
    }

    /**
     * Topic operations
     */

    override fun addTopic(topic: FlashcardTopicModel) {
        topic.topicId = getIdTopic()
        // Put topic in map and create empty list for flashcard deck
        flashcardMap.put(topic, ArrayList<FlashcardModel>())
        logAll()
    }

    override fun updateTopic(topic: FlashcardTopicModel) {
        /**
         * Workaround, due to the fact that keys in a map collection are unique (hashing) and cannot be overwritten
         * -> no editing in this way possible
         */
        // Get reference from map for topic to update
        var topicToUpdate = flashcardMap.keys.find { p -> p.topicId == topic.topicId }
        // Temporary store content (value) of map to updated topic
        var tempContent = flashcardMap.get(topicToUpdate)
        if (topicToUpdate != null && tempContent != null) {
            flashcardMap.remove(topicToUpdate)
            topicToUpdate.title = topic.title
            topicToUpdate.description = topic.description
            flashcardMap.put(topicToUpdate, tempContent)
        }

        logAll()
    }

    override fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        return flashcardMap.keys.toCollection(ArrayList<FlashcardTopicModel>())
    }

    override fun deleteTopic(topic: FlashcardTopicModel) {
        flashcardMap.remove(topic)
        logAll()
    }

    override fun setCurrentTopic(topic: FlashcardTopicModel) {
        workingTopic = topic
    }

    override fun getCurrentTopic(): FlashcardTopicModel {
        return workingTopic
    }

    /**
     * Map operations
     */

    override fun findFlashcardMap(): Map<FlashcardTopicModel, ArrayList<FlashcardModel>> {
        return flashcardMap
    }

    /**
     * Logging
     */

    private fun logAll() {
        // log info message
        flashcardMap.forEach { Timber.i(("$it")) }
    }


}