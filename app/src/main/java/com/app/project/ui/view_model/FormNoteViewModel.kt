package com.app.project.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.data_source.NoteDb
import com.app.project.data_source.NoteEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FormNoteViewModel : ViewModel() {

    private val _uiInteractionEvent = MutableSharedFlow<Int>()
    val uiInteractionEvent: SharedFlow<Int>
        get() = _uiInteractionEvent

    fun addNote(context: Context, note: NoteEntity) = viewModelScope.launch {
        NoteDb.getInstance(context).apply { noteDao().insert(note) }
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    fun editNote(context: Context, note: NoteEntity) = viewModelScope.launch {
        NoteDb.getInstance(context).apply { noteDao().update(note) }
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    fun excludeNote(context: Context, note: NoteEntity) = viewModelScope.launch {
        NoteDb.getInstance(context).apply { noteDao().delete(note) }
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    companion object {
        const val RESULT_NOTE_DB_CHANGED = 186
    }
}