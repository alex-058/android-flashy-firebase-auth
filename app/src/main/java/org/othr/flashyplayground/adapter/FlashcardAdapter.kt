package org.othr.flashyplayground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.databinding.RowPlacemarksBinding
import org.othr.flashyplayground.model.FlashcardModel

class FlashcardAdapter (private var flashcards: ArrayList<FlashcardModel>) : RecyclerView.Adapter<FlashcardAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = RowPlacemarksBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val flashcard = flashcards[holder.adapterPosition]
        holder.bind(flashcard)
    }

    override fun getItemCount(): Int = flashcards.size


    class MainHolder (private val binding: RowPlacemarksBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (flashcard: FlashcardModel) {
            binding.flashcardFrontContent.text = flashcard.front
        }
    }


}