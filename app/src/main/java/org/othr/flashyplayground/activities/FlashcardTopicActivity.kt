package org.othr.flashyplayground.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardTopicAddBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel
import timber.log.Timber


class FlashcardTopicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardTopicAddBinding
    var aTopic = FlashcardTopicModel()

    // Declaration of MainApp
    lateinit var app :  MainApp
    // Alert Dialog Builder to confirm deletion of topic
    lateinit var builder: AlertDialog.Builder
    var edit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Timber Tree for Logging support with Timber
        Timber.plant(Timber.DebugTree())

        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardTopicAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initalization of Main App
        app = application as MainApp

        // Setup Dialog if deleting a topic
        builder = AlertDialog.Builder(this@FlashcardTopicActivity)
        builder.setTitle(getString(R.string.title_confirm_delete))
        builder.setMessage(getString(R.string.message_confirm_delete_topic))

        // Toolbar support
        binding.toolbarTopicAdd.title = title
        setSupportActionBar(binding.toolbarTopicAdd)

        edit = false

        if (intent.hasExtra("edit_topic")) {
            aTopic = intent.extras?.getParcelable("edit_topic")!!
            binding.topicTitle.setText(aTopic.title)
            binding.topicDescription.setText(aTopic.description)
            // Reuse string variable
            binding.btnTopicAdd.setText(R.string.btn_changeFlashcard)
            edit = true
        }


        binding.btnTopicAdd.setOnClickListener {
            aTopic.title = binding.topicTitle.text.toString()
            aTopic.description = binding.topicDescription.text.toString()

            if (aTopic.title.isNotEmpty()) {
                if (edit == false) {
                    app.flashcards.addTopic(aTopic.copy())
                    Snackbar
                        .make(it, R.string.message_addedTopic, Snackbar.LENGTH_LONG)
                        .show()
                    // setResult(RESULT_OK)
                }
                else {
                    app.flashcards.updateTopic(aTopic.copy())
                }

                // Launch back list activity after adding / updating the topic
                var laucherIntent = Intent(applicationContext, SplashScreenActivity::class.java)
                startActivity(laucherIntent)
            }

            else {
                    Snackbar
                        .make(it, R.string.message_discardedAddTopic, Snackbar.LENGTH_LONG)
                        .show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_topic_add, menu)
        // enable / disable delete item with respect to edit mode
        if (edit) {
            menu?.findItem(R.id.topic_delete_item)?.setEnabled(true)
        }
        else {
            menu?.findItem(R.id.topic_delete_item)?.setEnabled(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.topic_delete_item -> {
                // yes / no button
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    app.flashcards.deleteTopic(aTopic.copy())
                    dialog.cancel() // back to main screen (onResume)
                    val launcherIntent = Intent(this, SplashScreenActivity::class.java)
                    startActivity(launcherIntent)
                })

                builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

                // make dialog happen
                var alert = builder.create()
                alert.show()

            }

            R.id.topic_cancel_item -> {
                Toast.makeText(this, R.string.message_canceled, Toast.LENGTH_LONG).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}