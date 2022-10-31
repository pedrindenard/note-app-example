package com.app.project.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.feature.data_source.NoteDb
import com.app.project.feature.data_source.NoteEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FormNoteViewModel : ViewModel() {

    private lateinit var dao: NoteDb

    private val _uiInteractionEvent = MutableSharedFlow<Int>()
    val uiInteractionEvent: SharedFlow<Int>
        get() = _uiInteractionEvent

    fun getDaoInstance(context: Context) = viewModelScope.launch {
        if (!this@FormNoteViewModel::dao.isInitialized) dao = NoteDb.getInstance(context)
    }

    fun addNote(note: NoteEntity) = viewModelScope.launch {
        dao.noteDao().insert(note)
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    fun editNote(note: NoteEntity) = viewModelScope.launch {
        dao.noteDao().update(note)
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    fun excludeNote(note: NoteEntity) = viewModelScope.launch {
        dao.noteDao().delete(note)
        _uiInteractionEvent.emit(RESULT_NOTE_DB_CHANGED)
    }

    companion object {
        const val RESULT_NOTE_DB_CHANGED = 186
    }
}