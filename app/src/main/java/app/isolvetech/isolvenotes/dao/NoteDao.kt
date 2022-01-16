package app.isolvetech.isolvenotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import app.isolvetech.isolvenotes.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun upDateNote(note: Note)

    @Query("SELECT  * FROM Note ORDER BY id DESC ")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE title LIKE :query OR content LIKE :query OR date LIKE :query ORDER BY id DESC")
    fun searchNote(query: String): LiveData<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)
}