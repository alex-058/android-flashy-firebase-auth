package org.othr.flashyplayground.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlashcardTopicModel (var topicId: Long = 0, var title: String = "", var description: String = "") : Parcelable{
}