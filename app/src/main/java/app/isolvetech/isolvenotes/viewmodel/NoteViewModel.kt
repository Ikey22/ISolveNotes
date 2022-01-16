package app.isolvetech.isolvenotes.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import app.isolvetech.isolvenotes.model.Note
import app.isolvetech.isolvenotes.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NoteViewModel(private val application: Application): ViewModel() {

     private val repository: NoteRepository by application.inject()
    fun saveNote(newNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(newNote)
    }

    fun updateNote(existingNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(existingNote)
    }

    fun deleteNote(existingNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(existingNote)
    }

    fun searchNote(query: String): LiveData<List<Note>> {
        return repository.searchNote(query)
    }

    fun getAllNotes(): LiveData<List<Note>> = repository.getNote()




    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }
}