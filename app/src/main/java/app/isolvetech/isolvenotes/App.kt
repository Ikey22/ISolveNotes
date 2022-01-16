package app.isolvetech.isolvenotes

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import app.isolvetech.isolvenotes.database.NoteDatabase
import app.isolvetech.isolvenotes.repository.NoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


class App: Application() {
    private var context: Context? = null

    private val appModule = module {

        single { NoteRepository(get()) }
        single { NoteDatabase(get()) }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}