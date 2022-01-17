package app.isolvetech.isolvenotes.adapters

import androidx.recyclerview.widget.DiffUtil
import app.isolvetech.isolvenotes.model.Note

class DiffUtilCallback: DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id==newItem.id
    }


    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id==newItem.id
    }

}
