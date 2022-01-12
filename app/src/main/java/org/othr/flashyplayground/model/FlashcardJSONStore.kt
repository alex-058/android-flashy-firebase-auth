package org.othr.flashyplayground.model

import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.*
import java.util.*
import kotlin.collections.LinkedHashMap
import android.content.Context
import org.othr.flashyplayground.helpers.*
import kotlin.coroutines.coroutineContext


const val JSON_FILE = "flashcards.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, FlashcardJSONStore.UriParser())
    .create()

val listType: Type = object : TypeToken<LinkedHashMap<FlashcardTopicModel, ArrayList<FlashcardModel>>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}
class FlashcardJSONStore(private val context: Context): FlashcardTopicStore {

    var flashcardMap =  mutableMapOf<FlashcardTopicModel, ArrayList<FlashcardModel>>()

    // stores topic for current working flashcard deck for convenience reasons
    lateinit var topic: FlashcardTopicModel

    override fun addFlashcard(flashcard: FlashcardModel) {
        TODO("Not yet implemented")
    }

    override fun updateFlashcard(flashcard: FlashcardModel) {
        TODO("Not yet implemented")
    }

    override fun deleteFlashcard(position: Int) {
        TODO("Not yet implemented")
    }

    override fun findAllFlashcards(): ArrayList<FlashcardModel> {
        TODO("Not yet implemented")
    }

    override fun findFlashcardCount(topic: FlashcardTopicModel): Int {
        TODO("Not yet implemented")
    }

    override fun addTopic(topic: FlashcardTopicModel) {
        TODO("Not yet implemented")
    }

    override fun updateTopic(topic: FlashcardTopicModel) {
        TODO("Not yet implemented")
    }

    override fun deleteTopic(topic: FlashcardTopicModel) {
        TODO("Not yet implemented")
    }

    override fun findAllTopics(): ArrayList<FlashcardTopicModel> {
        TODO("Not yet implemented")
    }

    override fun setCurrentTopic(topic: FlashcardTopicModel) {
        TODO("Not yet implemented")
    }

    override fun getCurrentTopic(): FlashcardTopicModel {
        TODO("Not yet implemented")
    }

    override fun findFlashcardMap(): Map<FlashcardTopicModel, ArrayList<FlashcardModel>> {
        TODO("Not yet implemented")
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