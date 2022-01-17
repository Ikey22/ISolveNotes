package app.isolvetech.isolvenotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.isolvetech.isolvenotes.R
import app.isolvetech.isolvenotes.databinding.NoteItemBinding
import app.isolvetech.isolvenotes.fragments.NoteFragmentDirections
import app.isolvetech.isolvenotes.model.Note
import app.isolvetech.isolvenotes.utils.hideKeyboard
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.commonmark.node.SoftLineBreak

class NotesAdapter: ListAdapter<Note, NotesAdapter.NoteViewHolder>(DiffUtilCallback()) {

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private val contentBinding = NoteItemBinding.bind(itemView)
            val title: MaterialTextView = contentBinding.noteItemTitle
            val date: MaterialTextView = contentBinding.noteDate
            val content: TextView = contentBinding.noteContentItem
            val parent: MaterialCardView = contentBinding.noteItemLayoutParent
            val markwon = Markwon.builder(itemView.context)
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TaskListPlugin.create(itemView.context))
                .usePlugin(object : AbstractMarkwonPlugin(){
                    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                        super.configureVisitor(builder)
                        builder.on(
                            SoftLineBreak::class.java
                        ){visitor, _ -> visitor.forceNewLine()}
                    }
                }).build()

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder((
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.note_item, parent, false)
                ))
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position).let { note ->
            holder.apply {
                parent.transitionName = "recyclerview_${note.id}"
                title.text = note.title
                markwon.setMarkdown(content,note.content)
                date.text = note.date
                parent.setCardBackgroundColor(note.color)

                parent.setOnClickListener {
                    val action=NoteFragmentDirections.actionNoteFragmentToEditFragment()
                        .setNote(note)
                    val extras= FragmentNavigatorExtras(parent to "recyclerview_${note.id}")
                    it.hideKeyboard()
                    Navigation.findNavController(it).navigate(action,extras)

                }
                content.setOnClickListener {
                    val action=NoteFragmentDirections.actionNoteFragmentToEditFragment()
                        .setNote(note)
                    val extras= FragmentNavigatorExtras(parent to "recyclerview_${note.id}")
                    it.hideKeyboard()
                    Navigation.findNavController(it).navigate(action,extras)
                }
            }
        }
    }

}