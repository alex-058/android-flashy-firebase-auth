package org.othr.flashyplayground.model

import android.net.Uri
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import com.squareup.picasso.Picasso

var previousIdFlashcard = 0L
var previousIdTopic = 0L

internal fun getIdFlashcard (): Long {
    return previousIdFlashcard++
}
internal fun getIdTopic (): Long {
    return previousIdTopic++
}

class FlashcardMemStore: FlashcardStore, FlashcardTopicStore {

    // private var flashcards = ArrayList<FlashcardModel>()
    // Connection between flashcards and topics
    var flashcardMap =  mutableMapOf<FlashcardTopicModel, ArrayList<FlashcardModel>>()
    // stores topic for current working flashcard deck for convenience reasons
    lateinit var topic: FlashcardTopicModel

    override fun addFlashcard(flashcard: FlashcardModel) {
        flashcard.id = getIdFlashcard()
        flashcardMap.get(topic)?.add(flashcard)
        logAll()
    }

    override fun findAllFlashcards(): ArrayList<FlashcardModel> {
        // retrieve flashcards from provided topic
        return flashcardMap.getValue(topic)
    }

    override fun updateFlashcard(flashcard: FlashcardModel) {
        // retrieve flashcard from map value to directly work on a list reference
        var foundFlashcard = flashcardMap.get(topic)?.find { p -> p.id == flashcard.id}
        if (foundFlashcard != null) {
            foundFlashcard.image = flashcard.image
            foundFlashcard.front = flashcard.front
            foundFlashcard.back = flashcard.back
            logAll()
        }

    }

    override fun addTopic(topic: FlashcardTopicModel) {
        topic.topicId = getIdTopic()
        // Put topic in map and create empty list for flashcard deck
        flashcardMap.put(topic, ArrayList<FlashcardModel>())
        logAll()
    }

    override fun updateTopic(topic: FlashcardTopicModel) {
        var foundTopic = flashcardMap.keys.toMutableList().find { p -> p.title == topic.title }
        if (foundTopic != null) {
            foundTopic.title = topic.title
            foundTopic.description = topic.description
        }
        logAll()
    }

    /**
     * Needed to display it on recycler view
     */
    override fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        return flashcardMap.keys.toCollection(ArrayList<FlashcardTopicModel>())
    }

    override fun setCurrentTopic(currentTopic: FlashcardTopicModel) {
        topic = currentTopic
    }

    private fun logAll() {
        // log info message
        flashcardMap.forEach { Timber.i(("$it")) }
    }


}