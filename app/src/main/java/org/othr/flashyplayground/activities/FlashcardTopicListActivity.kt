package org.othr.flashyplayground.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.othr.flashyplayground.adapter.FlashcardTopicAdapter
import org.othr.flashyplayground.databinding.ActivityFlashcardTopicListBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel

class FlashcardTopicListActivity : AppCompatActivity(), FlashcardTopicAdapter.FlashcardTopicListener {

    lateinit var app : MainApp
    private lateinit var binding: ActivityFlashcardTopicListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // support for binding
        binding = ActivityFlashcardTopicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp (demo creation)
        app = application as MainApp

        // Initialize recycler view
        val layoutManager = GridLayoutManager(applicationContext, 2)
        binding.recyclerViewFlashcardTopics.layoutManager = layoutManager
        binding.recyclerViewFlashcardTopics.adapter = FlashcardTopicAdapter(app.flashcardTopics.findAllTopics(), this)
    }

    /**
     * Event handling when clicking on an item on the flashcard topic recycler view
     * TODO: Just pass the flashcards property from the topic object to the FlashcardListActivity (deprecated)
     */
    override fun onFlashcardTopicClick(flashcardTopic: FlashcardTopicModel) {
        val launcherIntent = Intent(this, FlashcardListActivity::class.java)
        // TODO: Problem: FlashcardMemStore cannot be passed. With our logic we would need to pass the whole MemStore object (list class works with methods from MemStore) but this is not applicable
        // Solution: Pass data (flashcards) and initialize flashcardDeck (MemStore) inside of FlashcardListActivity
        launcherIntent.putParcelableArrayListExtra("flashcard_list", flashcardTopic.flashcards)
        startActivity(launcherIntent)
    }


}