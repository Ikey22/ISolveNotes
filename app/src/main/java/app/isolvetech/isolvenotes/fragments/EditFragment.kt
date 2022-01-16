package app.isolvetech.isolvenotes.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import app.isolvetech.isolvenotes.R
import app.isolvetech.isolvenotes.activities.MainActivity
import app.isolvetech.isolvenotes.databinding.BottomSheetBinding
import app.isolvetech.isolvenotes.databinding.FragmentEditBinding
import app.isolvetech.isolvenotes.model.Note
import app.isolvetech.isolvenotes.utils.hideKeyboard
import app.isolvetech.isolvenotes.viewmodel.NoteViewModel
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.definition.Kind
import java.text.SimpleDateFormat
import java.util.*


class EditFragment : Fragment(R.layout.fragment_edit) {

    private lateinit var navController: NavController
    private lateinit var contentBinding: FragmentEditBinding
    private var note: Note? = null
    private var color = -1
    private lateinit var result: String
    private val currentData = SimpleDateFormat.getInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: EditFragmentArgs by navArgs()

    private val viewModel: NoteViewModel by lazy {
        requireActivity().run {
            ViewModelProvider(
                this,
                NoteViewModel.Factory(this.application)
            )[NoteViewModel::class.java]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anim = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = Color.TRANSPARENT
            duration = 300L
        }
        sharedElementEnterTransition = anim
        sharedElementReturnTransition = anim
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding = FragmentEditBinding.bind(view)

        navController = Navigation.findNavController(view)
        val activity = activity as MainActivity

        ViewCompat.setTransitionName(
            contentBinding.noteParent,
            "recyclerview_${args.note?.id}"
        )

        contentBinding.btnBack.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
            updateNote()
        }

        contentBinding.saveNote.setOnClickListener {
            saveNote()
        }

        try {
            contentBinding.noteContent.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    contentBinding.bottomBar.visibility = View.VISIBLE
                    contentBinding.noteContent.setStylesBar(contentBinding.styleBar)
                }
                else {
                    contentBinding.bottomBar.visibility = View.GONE
                }
            }
        } catch (e: Throwable){
            Log.d("Tag", e.stackTraceToString())
        }

        contentBinding.fabColorPick.setOnClickListener {
            val bottomSheet = BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )

            val bottomSheetView: View = layoutInflater.inflate(R.layout.bottom_sheet, null)

            with(bottomSheet){
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding = BottomSheetBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener {
                        color = it
                        contentBinding.apply {
                            noteParent.setBackgroundColor(color)
                            toolBarNote.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor = color
                        }
                        bottomSheetBinding.bottomSheetParent.setBackgroundColor(color)
                    }
                }
                bottomSheetParent.setBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        //opens with existing note
        setUpNote()

    }

    private fun setUpNote() {
        val note = args.note
        val title = contentBinding.etTitle
        val content = contentBinding.noteContent
        val lastEdited = contentBinding.lasteEdited

        if (note == null){
            contentBinding.lasteEdited.text = getString(R.string.edited_on, SimpleDateFormat.getInstance().format(
                Date()
            ))
        }
        if (note != null){
            color = note.color
            title.setText(note.title)
            content.renderMD(note.content)
            lastEdited.text = getString(R.string.edited_on, note.date)
            contentBinding.apply {
                job.launch {
                    delay(10)
                    noteParent.setBackgroundColor(color)
                }
                toolBarNote.setBackgroundColor(color)
                bottomBar.setBackgroundColor(color)
            }
            activity?.window?.statusBarColor = note.color
        }
    }

    private fun saveNote() {
        if (contentBinding.noteContent.text.toString()
                .isEmpty() || contentBinding.etTitle.text.toString().isEmpty()
        ) {
            Toast.makeText(activity, "Something is empty", Toast.LENGTH_SHORT).show()
        } else {
            note = args.note
            when (note) {
                null -> {
                    viewModel.saveNote(
                        Note(
                            0,
                            contentBinding.etTitle.text.toString(),
                            contentBinding.noteContent.getMD(),
                            currentData,
                            color
                        )
                    )

                    result = "Note Saved"
                    setFragmentResult(
                        "key",
                        bundleOf("bundleKey" to result)
                    )

                    navController.navigate(EditFragmentDirections.actionEditFragmentToNoteFragment())
                }
                else -> {
                    updateNote()
                    navController.popBackStack()
                }
            }
        }
    }

    private fun updateNote() {
        if (note!=null) {
            viewModel.updateNote(
                Note(
                    note!!.id,
                    contentBinding.etTitle.text.toString(),
                    contentBinding.noteContent.getMD(),
                    currentData,
                    color
                )
            )
        }

    }
}