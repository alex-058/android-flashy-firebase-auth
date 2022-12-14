package org.othr.flashyplayground.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class FlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardAddBinding

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    var aFlashcard = FlashcardModel()

    // Declaration of MainApp
    lateinit var app :  MainApp

    // Initialize edit flag to check on update / add state (default: false -> add mode)
    var edit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Timber Tree for Logging support with Timber
        Timber.plant(Timber.DebugTree())

        super.onCreate(savedInstanceState)

        binding = ActivityFlashcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initalization of Main App
        app = application as MainApp

        var edit = false

        // Toolbar support
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        if (intent.hasExtra("edit_flashcard")) {
            // Retrieve flashcard data
            aFlashcard = intent.extras?.getParcelable("edit_flashcard")!!
            // Set text flashcard text fields in order to allow user to edit text of existing flashcards
            Picasso.get()
                .load(aFlashcard.image)
                .resize(800, 600)
                .into(binding.imageViewAddFlashcard)
            binding.flashcardBack.setText(aFlashcard.back)
            binding.flashcardFront.setText(aFlashcard.front)
            // Set new toolbar title
            binding.btnAdd.setText(R.string.btn_changeFlashcard)
            edit = true
        }
        else {
            // set cursor to front input to continue adding flashcards in order to improve user experience
            binding.flashcardFront.requestFocus()
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

                    // re-launch activity to directly add new flashcard
                    val intent = intent
                    finish()
                    startActivity(intent)
                }

                else {
                    // Update existing flashcard
                    app.flashcards.updateFlashcard(aFlashcard.copy())
                    Snackbar
                        .make(it, R.string.message_updatedFlashcard, Snackbar.LENGTH_LONG)
                        .show()
                    // finish activity after updating
                    finish()
                }

            }

            else {
                Snackbar
                    .make(it, R.string.message_discardedAddFlashcard, Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        // event handling for add image button
        binding.btnAddImage.setOnClickListener {
            Toast.makeText(applicationContext, getString(R.string.message_select_image), Toast.LENGTH_LONG).show()
            showImagePicker(imageIntentLauncher)
        }

        // trigger callback methods
        registerImageIntentCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flashcard_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Event handling for toolbar items
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
     * Callback method to deal with selected image and display image
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
                                    .resize(800, 600)
                                    .into(binding.imageViewAddFlashcard)
                            }
                        }

                        RESULT_CANCELED -> {
                            Toast.makeText(applicationContext, getString(R.string.message_image_not_loaded_success), Toast.LENGTH_LONG).show()
                        }
                    }

            }

    }
}