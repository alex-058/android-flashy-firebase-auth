package org.othr.flashyplayground.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.RowFlashcardTopicsBinding
import org.othr.flashyplayground.main.MainApp
import org.othr.flashyplayground.model.FlashcardTopicModel

class FlashcardTopicAdapter (private var flashcardTopics: ArrayList<FlashcardTopicModel>, private val listener: FlashcardTopicListener) : RecyclerView.Adapter<FlashcardTopicAdapter.MainHolder>() {

    interface FlashcardTopicListener {
        fun onFlashcardTopicClick(flashcardTopic: FlashcardTopicModel)
        fun onFlashcardTopicClickLong(flashcardTopic: FlashcardTopicModel)
        fun refreshFlashcardCount(flashcardTopic: FlashcardTopicModel): String
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

    class MainHolder (private val binding: RowFlashcardTopicsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (flashcardTopic: FlashcardTopicModel, listener: FlashcardTopicListener) {
            binding.flashcardTopicTitle.text = flashcardTopic.title
            binding.flashcardCount.text = listener.refreshFlashcardCount(flashcardTopic)
            binding.root.setOnClickListener { listener.onFlashcardTopicClick(flashcardTopic) }
            binding.root.setOnLongClickListener {
                // trigger edit feature
                listener.onFlashcardTopicClickLong(flashcardTopic)
                true
            }
        }
    }
}