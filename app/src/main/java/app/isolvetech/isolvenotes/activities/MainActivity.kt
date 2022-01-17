package app.isolvetech.isolvenotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import app.isolvetech.isolvenotes.R
import app.isolvetech.isolvenotes.databinding.ActivityMainBinding
import app.isolvetech.isolvenotes.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var noteViewModel: NoteViewModel
    private val REQUEST_CODDE_ADD_NOTE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.user_share))
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        bind.optionMenu.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

    }


}