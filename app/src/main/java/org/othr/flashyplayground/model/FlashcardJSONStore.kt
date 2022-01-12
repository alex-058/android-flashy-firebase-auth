package org.othr.flashyplayground.model

import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.*
import java.util.*
import android.content.Context
import org.othr.flashyplayground.helpers.*


const val JSON_FILE = "flashcards.json"

/**
 * TODO: Is the enableComplexMapKeySerialization() needed?
 */
val gsonBuilder: Gson = GsonBuilder().enableComplexMapKeySerialization()
    .setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, FlashcardJSONStore.UriParser())
    .create()

val listType: Type = object : TypeToken<Map<FlashcardTopicModel, ArrayList<FlashcardModel>>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}
class FlashcardJSONStore(private val context: Context): FlashcardTopicStore {

    var flashcardMap =  mutableMapOf<FlashcardTopicModel, ArrayList<FlashcardModel>>()

    // stores topic for current working flashcard deck for convenience reasons
    lateinit var workingTopic: FlashcardTopicModel

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    /**
     * Flashcard operations
     */

    override fun addFlashcard(flashcard: FlashcardModel) {
        flashcard.id = generateRandomId()
        flashcardMap.get(workingTopic)?.add(flashcard)
        logAll()
        serialize()
    }

    override fun updateFlashcard(flashcard: FlashcardModel) {
        var foundFlashcard = flashcardMap.get(workingTopic)?.find { p -> p.id == flashcard.id}
        if (foundFlashcard != null) {
            foundFlashcard.image = flashcard.image
            foundFlashcard.front = flashcard.front
            foundFlashcard.back = flashcard.back
            logAll()
            serialize()
        }
    }

    override fun deleteFlashcard(position: Int) {
        findAllFlashcards().removeAt(position)
        logAll()
        serialize()
    }

    override fun findAllFlashcards(): ArrayList<FlashcardModel> {
        return flashcardMap.getValue(workingTopic)
    }

    override fun findFlashcardCount(topic: FlashcardTopicModel): Int {
        return flashcardMap.getValue(topic).size
    }

    /**
     * Topic operations
     */

    override fun addTopic(topic: FlashcardTopicModel) {
        topic.topicId = generateRandomId()
        // Put topic in map and create empty list for flashcard deck
        flashcardMap.put(topic, ArrayList<FlashcardModel>())
        logAll()
        serialize()
    }

    override fun updateTopic(topic: FlashcardTopicModel) {
        // Get reference from map for topic to update
        var topicToUpdate = flashcardMap.keys.find { p -> p.topicId == topic.topicId }
        // Temporary store content (value) of map to updated topic
        var tempContent = flashcardMap.get(topicToUpdate)
        if (topicToUpdate != null && tempContent != null) {
            flashcardMap.remove(topicToUpdate)
            topicToUpdate.title = topic.title
            topicToUpdate.description = topic.description
            flashcardMap.put(topicToUpdate, tempContent)
            logAll()
            serialize()
        }


    }

    override fun deleteTopic(topic: FlashcardTopicModel) {
        flashcardMap.remove(topic)
        logAll()
        serialize()
    }

    override fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        return flashcardMap.keys.toCollection(ArrayList<FlashcardTopicModel>())
    }

    override fun setCurrentTopic(topic: FlashcardTopicModel) {
        workingTopic = topic
    }

    override fun getCurrentTopic(): FlashcardTopicModel {
        return workingTopic
    }

    override fun findFlashcardMap(): Map<FlashcardTopicModel, ArrayList<FlashcardModel>> {
        return flashcardMap
    }

    private fun logAll() {
        // log info message
        flashcardMap.forEach { Timber.i(("$it")) }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(flashcardMap, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize () {
        val jsonString = read(context, JSON_FILE)
        flashcardMap = gsonBuilder.fromJson(jsonString, listType)
    }

    /**
     * Required to successfully parse the Uri image property in our flashcard model.
     */
    class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Uri {
            return Uri.parse(json?.asString)
        }

        override fun serialize(
            src: Uri?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src.toString())
        }
    }

}