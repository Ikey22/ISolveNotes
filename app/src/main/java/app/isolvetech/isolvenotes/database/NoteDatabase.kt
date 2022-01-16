package app.isolvetech.isolvenotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.isolvetech.isolvenotes.dao.NoteDao
import app.isolvetech.isolvenotes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDataBase(context).also {
                instance = it
            }
        }

        fun createDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "iSolveNote_database"
        ).build()
    }

    abstract fun getNoteDao(): NoteDao
    //abstract fun getNoteDao(): NoteDao

}

//private lateinit var INSTANCE: NoteDatabase
//
//fun dataBase(context: Context): NoteDatabase {
//    synchronized(NoteDatabase::class.java) {
//
//        INSTANCE = Room.databaseBuilder(
//            context.applicationContext,
//            NoteDatabase::class.java,
//            "iSolveNote_database"
//        ).build()
//    }
//    return INSTANCE




