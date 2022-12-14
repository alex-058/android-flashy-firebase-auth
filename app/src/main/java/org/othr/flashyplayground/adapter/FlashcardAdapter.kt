package org.othr.flashyplayground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.databinding.RowFlashcardsBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardAdapter (private var flashcards: ArrayList<FlashcardModel>, private val listener: FlashcardListener) : RecyclerView.Adapter<FlashcardAdapter.MainHolder>() {

    // Introduce Listener Interface for clicking on certain elements in the recycler view
    interface FlashcardListener {
        fun onFlashcardItemClickLong(flashcard: FlashcardModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = RowFlashcardsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val flashcard = flashcards[holder.adapterPosition]
        holder.bind(flashcard, listener)
    }

    override fun getItemCount(): Int = flashcards.size


    // Operate on the specific row (flashcard) of the recycler view
    class MainHolder (private val binding: RowFlashcardsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (flashcard: FlashcardModel, listener: FlashcardListener) {
            binding.flashcardFrontContent.text = flashcard.front
            binding.root.setOnLongClickListener {
                listener.onFlashcardItemClickLong(flashcard)
                true
            }
        }
    }


}