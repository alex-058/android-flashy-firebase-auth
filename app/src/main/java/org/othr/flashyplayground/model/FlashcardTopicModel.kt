package org.othr.flashyplayground.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FlashcardTopicModel (var title: String = "", var description: String = "", var flashcards: ArrayList<FlashcardModel> = arrayListOf()) : Parcelable{
}