package app.isolvetech.isolvenotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.isolvetech.isolvenotes.databinding.ActivityMainBinding
import app.isolvetech.isolvenotes.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    private val REQUEST_CODDE_ADD_NOTE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

    }
}