package app.isolvetech.isolvenotes.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val title: String,
    val content: String,
    val date: String,
    val color: Int = -1,

    ) : Serializable