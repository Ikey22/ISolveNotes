package app.isolvetech.isolvenotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.isolvetech.isolvenotes.R
import app.isolvetech.isolvenotes.database.NoteDatabase
import app.isolvetech.isolvenotes.databinding.ActivityMainBinding
import app.isolvetech.isolvenotes.repository.NoteRepository
import app.isolvetech.isolvenotes.viewmodel.NoteActivityViewModelFactory
import app.isolvetech.isolvenotes.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    private val REQUEST_CODDE_ADD_NOTE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        bind = ActivityMainBinding.inflate(layoutInflater)

        try {
            setContentView(bind.root)
            val noteRepository = NoteRepository(NoteDatabase(this))
            val noteActivityViewModelFactory = NoteActivityViewModelFactory(noteRepository)
            noteViewModel = ViewModelProvider(this, noteActivityViewModelFactory)[NoteViewModel::class.java]
        } catch (e: Exception) {
            Log.d("TAG", "Error")
        }
    }
}