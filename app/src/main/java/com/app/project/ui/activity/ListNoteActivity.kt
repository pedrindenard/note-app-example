package com.app.project.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.ui.adapter.NoteAdapter
import com.app.project.databinding.ActivityListNoteBinding
import com.app.project.feature.enums.Interaction
import com.app.project.utils.Utils.interaction
import com.app.project.ui.view_model.FormNoteViewModel
import com.app.project.ui.view_model.ListNoteViewModel
import com.app.project.utils.Utils
import com.app.project.utils.Utils.note

class ListNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListNoteBinding

    private val viewModel by lazy { ViewModelProvider(this)[ListNoteViewModel::class.java] }
    private val mainAdapter by lazy { NoteAdapter() }

    private val activity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            FormNoteViewModel.RESULT_NOTE_DB_CHANGED -> {
                viewModel.getNotes(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapterListener()
        setListeners()
        setObservers()
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotes(this)
    }

    private fun setListeners() {
        binding.activityListNoteAdd.setOnClickListener {
            val intent = Intent(this, FormNoteActivity::class.java)
            intent.interaction = Interaction.ADD_NOTE
            activity.launch(intent)
        }
    }

    private fun setObservers() {
        viewModel.uiResultEvent.observe(this) { notes ->
            binding.activityListNoteEmpty.root.isVisible = notes.isEmpty()
            mainAdapter.insertItems(notes)
        }
    }

    private fun setAdapter() {
        binding.activityListNoteRecyclerView.adapter = mainAdapter
        binding.activityListNoteRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setAdapterListener() {
        mainAdapter.setItemClickListener(object : NoteAdapter.ItemClickListener {
            override fun onClickListener(position: Int) {
                val intent = Intent(this@ListNoteActivity, FormNoteActivity::class.java)
                intent.interaction = Interaction.EDIT_NOTE
                intent.note = mainAdapter.items[position]
                activity.launch(intent)
            }

            override fun onLongClickListener(position: Int) {
                Utils.showDialogAlert(this@ListNoteActivity) {
                    viewModel.removeNote(this@ListNoteActivity, mainAdapter.items[position])
                    mainAdapter.removeItem(position)
                }
            }
        })
    }
}