package org.othr.flashyplayground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.databinding.RowFlashcardTopicsBinding
import org.othr.flashyplayground.model.FlashcardTopicModel

class FlashcardTopicAdapter (private var flashcardTopics: ArrayList<FlashcardTopicModel>, private val listener: FlashcardTopicListener) : RecyclerView.Adapter<FlashcardTopicAdapter.MainHolder>() {

    interface FlashcardTopicListener {
        fun onFlashcardTopicClick(flashcardTopic: FlashcardTopicModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = RowFlashcardTopicsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val flashcardTopic = flashcardTopics[holder.adapterPosition]
        holder.bind(flashcardTopic, listener)
    }

    override fun getItemCount(): Int {
        return flashcardTopics.size
    }

    // operate on the specific rows of the recycler view / RowFlashcardBinding
    class MainHolder (private val binding: RowFlashcardTopicsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (flashcardTopic: FlashcardTopicModel, listener: FlashcardTopicListener) {
            binding.flashcardTopicTitle.text = flashcardTopic.title
            binding.root.setOnClickListener { listener.onFlashcardTopicClick(flashcardTopic) }
        }
    }
}