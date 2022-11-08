package com.app.project.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.project.R
import com.app.project.databinding.ActivityFormNoteBinding
import com.app.project.feature.data_source.NoteEntity
import com.app.project.feature.enums.Color
import com.app.project.feature.enums.Interaction
import com.app.project.ui.dialog.ColorDialog
import com.app.project.ui.view_model.FormNoteViewModel
import com.app.project.utils.Utils
import com.app.project.utils.Utils.interaction
import com.app.project.utils.Utils.note
import com.app.project.utils.Utils.toEditable
import kotlinx.coroutines.flow.collectLatest

class FormNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormNoteBinding
    private lateinit var viewModel: FormNoteViewModel
    private var color: Color = Color.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNoteContent()
        setViewModel()
        setListeners()
        setObservers()
        getColor()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDaoInstance(this)
    }

    private fun setListeners() {
        binding.activityFormNoteDelete.setOnClickListener {
            excludeNote()
        }

        binding.activityFormNoteColor.setOnClickListener {
            showDialogColor()
        }

        binding.activityFormNoteCheck.setOnClickListener {
            doNote()
        }

        binding.activityFormNoteClose.setOnClickListener {
            finish()
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[FormNoteViewModel::class.java]
    }

    private fun setNoteContent() {
        binding.activityFormNoteTitle.text = intent.note?.title?.toEditable()
        binding.activityFormNoteDescription.text = intent.note?.description?.toEditable()

        when (intent.interaction) {
            Interaction.ADD_NOTE -> {
                binding.activityFormNoteMenu.text = getString(R.string.activity_form_note_new)
                binding.activityFormNoteDelete.visibility = View.GONE
            }
            Interaction.EDIT_NOTE -> {
                binding.activityFormNoteMenu.text = getString(R.string.activity_form_note_edit)
                binding.activityFormNoteDelete.visibility = View.VISIBLE
            }
        }
    }

    private fun getColor() {
        val enum = intent.note?.color
        if (enum != null) {
            setButtonColor(Utils.getNoteColor(enum))
            color = Utils.getNoteColor(enum)
        }
    }

    private fun setObservers() {
        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiInteractionEvent.collectLatest { result ->
                    setResult(result)
                    finish()
                }
            }
        }
    }

    private fun doNote() {
        val title = binding.activityFormNoteTitle.text.toString()
        val description = binding.activityFormNoteDescription.text.toString()

        if (title.isEmpty() || title.isBlank()) {
            Toast.makeText(this, getString(R.string.msg_title_epy), Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty() || description.isBlank()) {
            Toast.makeText(this, getString(R.string.msg_desc_epy), Toast.LENGTH_SHORT).show()
            return
        }

        doInteraction()
    }

    private fun doInteraction() {
        val title = binding.activityFormNoteTitle.text.toString()
        val description = binding.activityFormNoteDescription.text.toString()
        val currentDate = Utils.getCalendarDate()
        val color = color.toString().lowercase()
        val id = intent.note?.id ?: 0

        val noteEntity = NoteEntity(id, title, description, currentDate, color)
        intent.note = noteEntity

        when (intent.interaction) {
            Interaction.ADD_NOTE -> viewModel.addNote(noteEntity)
            Interaction.EDIT_NOTE -> viewModel.editNote(noteEntity)
        }
    }

    private fun excludeNote() {
        if (intent.interaction == Interaction.EDIT_NOTE && intent.note != null) {
            Utils.showDialogAlert(this) {
                viewModel.excludeNote(intent.note!!)
            }
        }
    }

    private fun showDialogColor() {
        val dialog = ColorDialog(color)
        dialog.show(supportFragmentManager, "")
        dialog.setMenuClickListener(object : ColorDialog.MenuClickListener {
            override fun onClick(value: Color) {
                setButtonColor(value)
                color = value
            }
        })
    }

    private fun setButtonColor(value: Color) {
        binding.activityFormNoteColor.setColorFilter(
            ContextCompat.getColor(
                binding.root.context, when (value) {
                    Color.RED -> R.color.note_red
                    Color.GREEN -> R.color.note_green
                    Color.YELLOW -> R.color.button_yellow
                    Color.NONE -> R.color.note_gray
                }
            )
        )
    }
}