package app.isolvetech.isolvenotes.repository

import androidx.room.Query
import app.isolvetech.isolvenotes.database.NoteDatabase
import app.isolvetech.isolvenotes.model.Note

class NoteRepository(private val db: NoteDatabase) {

    fun getNote() = db.getNoteDao().getAllNotes()

    fun searchNote(query: String) = db.getNoteDao().searchNote(query)

    suspend fun addNote(note: Note) = db.getNoteDao().addNote(note)

    suspend fun updateNote(note: Note) = db.getNoteDao().upDateNote(note)

    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)


}