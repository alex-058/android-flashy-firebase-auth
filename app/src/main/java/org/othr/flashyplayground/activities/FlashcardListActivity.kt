package org.othr.flashyplayground.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityFlashcardListBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.adapter.FlashcardAdapter
import org.othr.flashyplayground.main.MainApp

class FlashcardListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashcardListBinding
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Support for Binding
        binding = ActivityFlashcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization of MainApp
        app = application as MainApp


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFlashcards.layoutManager = layoutManager
        binding.recyclerViewFlashcards.adapter = FlashcardAdapter(app.flashcards)

    }
}