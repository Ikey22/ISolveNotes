package app.isolvetech.isolvenotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.isolvetech.isolvenotes.repository.NoteRepository

class NoteActivityViewModelFactory(private val repository: NoteRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }
}