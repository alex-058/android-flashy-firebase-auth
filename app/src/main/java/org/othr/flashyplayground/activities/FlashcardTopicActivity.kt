package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize Timber Tree for Logging support with Timber
        Timber.plant(Timber.DebugTree())

        super.onCreate(savedInstanceState)
        binding = ActivityFlashcardTopicAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var edit = false

        if (intent.hasExtra("edit_topic")) {
            aTopic = intent.extras?.getParcelable("edit_topic")!!
            binding.topicTitle.setText(aTopic.title)
            binding.topicDescription.setText(aTopic.description)
            // Reuse string variable
            binding.btnTopicAdd.setText(R.string.btn_changeFlashcard)
            edit = true
        }

        // Initalization of Main App
        app = application as MainApp

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
            }

            // Always launch back list activity after adding / updating the topic

            var laucherIntent = Intent(applicationContext, FlashcardTopicListActivity::class.java)
            startActivity(laucherIntent)

        }
    }
}