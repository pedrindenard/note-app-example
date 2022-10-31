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

    private val _uiResultEvent = MutableLiveData<List<NoteEntity>>()
    val uiResultEvent: LiveData<List<NoteEntity>>
        get() = _uiResultEvent

    fun getNotes(context: Context) = viewModelScope.launch {
        NoteDb.getInstance(context).apply {
            val result = noteDao().get() ?: emptyList()
            _uiResultEvent.postValue(result)
        }
    }

    fun removeNote(context: Context, note: NoteEntity) = viewModelScope.launch {
        NoteDb.getInstance(context).apply {
            noteDao().delete(note)
        }
    }
}