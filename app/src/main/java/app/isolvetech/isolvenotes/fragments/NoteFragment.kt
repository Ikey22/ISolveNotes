package app.isolvetech.isolvenotes.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.isolvetech.isolvenotes.R
import app.isolvetech.isolvenotes.activities.MainActivity
import app.isolvetech.isolvenotes.adapters.NotesAdapter
import app.isolvetech.isolvenotes.databinding.FragmentNoteBinding
import app.isolvetech.isolvenotes.utils.SwipeToDelete
import app.isolvetech.isolvenotes.utils.hideKeyboard
import app.isolvetech.isolvenotes.viewmodel.NoteViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class NoteFragment : Fragment(R.layout.fragment_note) {
    private lateinit var noteBinding: FragmentNoteBinding
    private lateinit var rvAdapter: NotesAdapter

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
        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }
        enterTransition = MaterialElevationScale(false).apply {
            duration = 350
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteBinding = FragmentNoteBinding.bind(view)
        val activity = activity as MainActivity
        val navController = Navigation.findNavController(view)
        requireView().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            delay(10)
            //activity.window.statusBarColor = Color.WHITE
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.parseColor("#9E9D9D")
        }

        noteBinding.addNoteFab.setOnClickListener {
            noteBinding.appBarLayout.visibility = View.INVISIBLE
            navController.navigate(R.id.action_noteFragment_to_editFragment)
        }
        noteBinding.innerFab.setOnClickListener {
            noteBinding.appBarLayout.visibility = View.INVISIBLE
            navController.navigate(R.id.action_noteFragment_to_editFragment)
        }

        recyclerViewDisplay()

        swipeToDelete(noteBinding.rvNote)

        noteBinding.search.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                noteBinding.noData.isVisible = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    val text = s.toString()
                    val query = "%$text%"
                    if (query.isNotEmpty()){
                        viewModel.searchNote(query).observe(viewLifecycleOwner){
                            rvAdapter.submitList(it)
                        }
                    }
                    else{
                        observeDataChanges()
                    }
                }
                else {
                    observeDataChanges()
                }
            }


            override fun afterTextChanged(s: Editable?) {

            }

        })

        noteBinding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                v.clearFocus()
                requireView().hideKeyboard()
            }
            return@setOnEditorActionListener true
        }

        noteBinding.rvNote.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            when {
                scrollY>oldScrollY -> {
                    noteBinding.fabText.isVisible = false
                }
                scrollX==scrollY -> {
                    noteBinding.fabText.isVisible = true
                }
                else -> {
                    noteBinding.fabText.isVisible = true
                }
            }
        }

    }

    private fun swipeToDelete(rvNote: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val note = rvAdapter.currentList[position]
                var actionBtnTapped = false
                viewModel.deleteNote(note)
                noteBinding.search.apply {
                    hideKeyboard()
                    clearFocus()
                }
                if (noteBinding.search.text.toString().isEmpty()){
                    observeDataChanges()
                }
                val snackBar = Snackbar.make(
                    requireView(), "Note Deleted", Snackbar.LENGTH_LONG
                ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                    }

                    override fun onShown(transientBottomBar: Snackbar?) {
                        transientBottomBar?.setAction("UNDO"){
                            viewModel.saveNote(note)
                            actionBtnTapped = true
                            noteBinding.noData.isVisible = false

                        }

                        super.onShown(transientBottomBar)

                    }
                }).apply {
                    animationMode=Snackbar.ANIMATION_MODE_FADE
                    setAnchorView(R.id.addNoteFab)
                }
                snackBar.setActionTextColor(
                    ContextCompat.getColor(requireContext(),R.color.yellowOrange)
                )
                snackBar.show()
            }

        }

        val itemTouchHelper=ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvNote)
    }

    private fun observeDataChanges() {
        viewModel.getAllNotes().observe(viewLifecycleOwner) {
            noteBinding.noData.isVisible = it.isEmpty()
            rvAdapter.submitList(it)
        }

    }

    private fun recyclerViewDisplay() {

        when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> setUpRecyclerView(2)
            Configuration.ORIENTATION_LANDSCAPE -> setUpRecyclerView(3)
        }
    }

    private fun setUpRecyclerView(spanCount: Int) {
            noteBinding.rvNote.apply {
                layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                rvAdapter = NotesAdapter()
                rvAdapter.stateRestorationPolicy=
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                adapter=rvAdapter
                postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        observeDataChanges()
    }

}