package app.isolvetech.isolvenotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.isolvetech.isolvenotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMainBinding
    private val REQUEST_CODDE_ADD_NOTE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bind.imageAddNoteMain.setOnClickListener {
            startActivityForResult(Intent(this, CreateNoteActivity::class.java), REQUEST_CODDE_ADD_NOTE)
        }
    }
}