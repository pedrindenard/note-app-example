package com.app.project.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.project.R
import com.app.project.databinding.DialogColorBinding
import com.app.project.feature.enums.Color
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ColorDialog constructor() : BottomSheetDialogFragment() {

    private lateinit var menuClickListener: MenuClickListener

    private var _binding: DialogColorBinding? = null
    private val binding get() = _binding!!

    constructor(color: Color) : this() {
        lifecycleScope.launchWhenResumed {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                when (color) {
                    Color.RED -> binding.dialogButtonRed.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.note_red)
                    )
                    Color.GREEN -> binding.dialogButtonGreen.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.note_green)
                    )
                    Color.YELLOW -> binding.dialogButtonYellow.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.button_yellow)
                    )
                    Color.NONE -> binding.dialogButtonNone.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.note_gray)
                    )
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?): View {
        _binding = DialogColorBinding.inflate(inflater, view, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.dialogButtonRed.setOnClickListener {
            menuClickListener.onClick(Color.RED)
            dismiss()
        }

        binding.dialogButtonGreen.setOnClickListener {
            menuClickListener.onClick(Color.GREEN)
            dismiss()
        }

        binding.dialogButtonYellow.setOnClickListener {
            menuClickListener.onClick(Color.YELLOW)
            dismiss()
        }

        binding.dialogButtonNone.setOnClickListener {
            menuClickListener.onClick(Color.NONE)
            dismiss()
        }
    }

    fun setMenuClickListener(mMenuClickListener: MenuClickListener) {
        menuClickListener = mMenuClickListener
    }

    interface MenuClickListener {
        fun onClick(value: Color)
    }
}