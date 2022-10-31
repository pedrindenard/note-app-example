package com.app.project.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.feature.data_source.NoteDb
import com.app.project.feature.data_source.NoteEntity
import kotlinx.coroutines.launch

class ListNoteViewModel : ViewModel() {

    private lateinit var dao: NoteDb

    private val _uiResultEvent = MutableLiveData<List<NoteEntity>>()
    val uiResultEvent: LiveData<List<NoteEntity>>
        get() = _uiResultEvent

    fun getDaoInstance(context: Context) = viewModelScope.launch {
        if (!this@ListNoteViewModel::dao.isInitialized) dao = NoteDb.getInstance(context)
    }

    fun getNotes() = viewModelScope.launch {
        _uiResultEvent.postValue(dao.noteDao().get() ?: emptyList())
    }

    fun removeNote(note: NoteEntity) = viewModelScope.launch {
        dao.noteDao().delete(note)
    }
}