package app.isolvetech.isolvenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.isolvetech.isolvenotes.databinding.ActivityCreateNoteBinding

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var bind : ActivityCreateNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_create_note)

        bind.imageBack.setOnClickListener {
            onBackPressed()
        }
    }
}