package org.othr.flashyplayground.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlashcardModel (var front: String = "", var back: String = "", var id: Long = 0): Parcelable {
}