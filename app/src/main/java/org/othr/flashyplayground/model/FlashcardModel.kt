package org.othr.flashyplayground.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlashcardModel (var front: String = "", var back: String = "", var id: Long = 0, var image: Uri = Uri.EMPTY): Parcelable {
}