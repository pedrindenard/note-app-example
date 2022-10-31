package com.app.project.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.text.Editable
import androidx.core.content.ContextCompat
import com.app.project.R
import com.app.project.data_source.NoteEntity
import com.app.project.enums.Interaction
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    var Intent.interaction: Interaction
        get() = getSerializableExtra("interaction") as Interaction
        set(value) {
            putExtra("interaction", value as Serializable)
        }

    var Intent.note: NoteEntity?
        get() = getSerializableExtra("note") as NoteEntity?
        set(value) {
            putExtra("note", value as Serializable)
        }

    @SuppressLint("SimpleDateFormat")
    fun getCalendarDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun showDialogAlert(activity: Activity, onPositive: () -> Unit) {
        val builder = AlertDialog.Builder(activity)

        builder.setMessage(activity.getString(R.string.msg_exclude))
        builder.setPositiveButton(activity.getString(R.string.msg_confirm)) { dialog, _ ->
            onPositive.invoke()
            dialog.dismiss()
        }

        builder.setNegativeButton(activity.getString(R.string.msg_cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create()
        builder.show().apply {
            val red = ContextCompat.getColor(activity, R.color.note_red)
            val green = ContextCompat.getColor(activity, R.color.note_green)

            getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(green)
                isAllCaps = false
            }

            getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColor(red)
                isAllCaps = false
            }
        }
    }
}