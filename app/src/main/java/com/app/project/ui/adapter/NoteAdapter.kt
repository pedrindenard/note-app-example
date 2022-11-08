package com.app.project.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.project.R
import com.app.project.databinding.ItemNoteBinding
import com.app.project.feature.data_source.NoteEntity
import com.app.project.feature.enums.Color
import com.app.project.utils.Utils

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    val items = arrayListOf<NoteEntity>()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int =
        items.size

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteEntity) {
            binding.itemNoteTitle.text = note.title
            binding.itemNoteDescription.text = note.description
            binding.itemNoteDate.text = Utils.formatDateToNationality(note.update)

            binding.itemNoteColor.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context, when (Utils.getNoteColor(note.color)) {
                        Color.RED -> R.color.note_red
                        Color.GREEN -> R.color.note_green
                        Color.YELLOW -> R.color.button_yellow
                        Color.NONE -> R.color.note_gray
                    }
                )
            )

            binding.root.setOnClickListener {
                itemClickListener.onClickListener(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                itemClickListener.onLongClickListener(adapterPosition)
                false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun insertItems(newList: List<NoteEntity>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setItemClickListener(mItemClickListener: ItemClickListener) {
        itemClickListener = mItemClickListener
    }

    interface ItemClickListener {
        fun onClickListener(position: Int)
        fun onLongClickListener(position: Int)
    }
}