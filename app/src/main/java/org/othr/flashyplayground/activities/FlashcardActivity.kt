package org.othr.flashyplayground.activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.othr.flashyplayground.databinding.ActivityFlashcardAddBinding
import org.othr.flashyplayground.model.FlashcardModel
import com.google.android.material.snackbar.Snackbar
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.R
import timber.log.Timber
import org.othr.flashyplayground.helpers.*
import com.squareup.picasso.Picasso
import org.othr.flashyplayground.model.FlashcardTopicModel
import java.net.URI

class FlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardAddBinding

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    var aFlashcard = FlashcardModel()

    lateinit var topic: FlashcardTopicModel

    // Declaration of MainApp
    lateinit var app :  MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Timber Tree for Logging support with Timber
        Timber.plant(Timber.DebugTree())

        super.onCreate(savedInstanceState)
        // Binding Support
        binding = ActivityFlashcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initalization of Main App
        app = application as MainApp

        // Initialize edit flag to check on update / add state (default: false -> add mode)
        var edit = false // is this the right position to declare this variable?

        // Toolbar support
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        if (intent.hasExtra("edit_flashcard")) {
            // Retrieve flashcard data
            aFlashcard = intent.extras?.getParcelable("edit_flashcard")!!
            // Set text flashcard text fields in order to allow user to edit text of existing flashcards
            Picasso.get()
                .load(aFlashcard.image)
                .into(binding.imageViewAddFlashcard)
            binding.flashcardBack.setText(aFlashcard.back)
            binding.flashcardFront.setText(aFlashcard.front)
            // Set new toolbar title
            binding.btnAdd.setText(R.string.btn_changeFlashcard)
            edit = true
        }

        binding.btnAdd.setOnClickListener {
            aFlashcard.front = binding.flashcardFront.text.toString()
            aFlashcard.back  = binding.flashcardBack.text.toString()

            if (aFlashcard.front.isNotEmpty() && aFlashcard.back.isNotEmpty()) {

                if (edit == false) {
                    // Add new flashcard to list
                    app.flashcards.addFlashcard(aFlashcard.copy())
                    Snackbar
                        .make(it, R.string.message_addedFlashcard, Snackbar.LENGTH_LONG)
                        .show()

                    // reset current flashcard an image to add new items
                    aFlashcard = FlashcardModel()
                    binding.imageViewAddFlashcard.visibility = View.GONE
                }

                else {
                    // Update existing flashcard
                    app.flashcards.updateFlashcard(aFlashcard.copy())
                    Snackbar
                        .make(it, R.string.message_updatedFlashcard, Snackbar.LENGTH_LONG)
                        .show()
                    finish()
                }

                // Request focus operations and hide if present
                binding.flashcardFront.text.clear()
                binding.flashcardBack.text.clear()

                // set cursor back to the front
                binding.flashcardFront.requestFocus()

            }

            else {
                Snackbar
                    .make(it, R.string.message_discardedAddFlashcard, Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        binding.btnAddImage.setOnClickListener {
            Toast.makeText(applicationContext, "Please select an image", Toast.LENGTH_LONG).show()
            showImagePicker(imageIntentLauncher)
        }

        // trigger callback methods
        registerImageIntentCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel_item -> {
                // Launch Flashcard List Activity
                val launcherIntent = Intent(this, FlashcardListActivity::class.java)
                Toast.makeText(applicationContext, R.string.message_canceled, Toast.LENGTH_SHORT).show()
                // simply start activity without looking for a result
                startActivity(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Callback methods
     */

    private fun registerImageIntentCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                result ->
                    when (result.resultCode) {
                        // if an image was successfully selected
                        RESULT_OK -> {
                            if (result.data != null) {
                                // event handling for selected image (data) with Picasso
                                aFlashcard.image = result.data!!.data!!
                                // load stored image in image view with Picasso
                                Picasso.get()
                                    .load(aFlashcard.image)
                                    .into(binding.imageViewAddFlashcard)
                            }
                        }

                        RESULT_CANCELED -> {
                            Toast.makeText(applicationContext, "Image not loaded successfully", Toast.LENGTH_LONG).show()
                        }
                    }

            }

    }
}