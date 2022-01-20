package org.othr.flashyplayground.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.othr.flashyplayground.R

/**
 * Helper method to launch image picker - will be triggered and coped with in flashcard add activity
 */

fun showImagePicker (intentLauncher: ActivityResultLauncher<Intent>) {
    var file = Intent(Intent.ACTION_OPEN_DOCUMENT)
    file.type = "image/*"
    file = Intent.createChooser(file, R.string.select_image_for_flashcard.toString())
    intentLauncher.launch(file)
}