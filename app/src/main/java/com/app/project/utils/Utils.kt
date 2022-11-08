package com.app.project.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.text.Editable
import androidx.core.content.ContextCompat
import com.app.project.R
import com.app.project.feature.data_source.NoteEntity
import com.app.project.feature.enums.Color
import com.app.project.feature.enums.Interaction
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val RED = "red"
    private const val GREEN = "green"
    private const val YELLOW = "yellow"

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

    fun formatDateToNationality(currentDate: String): String {
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-DD", Locale.ROOT)
        val date = formatter.parse(currentDate) as Date
        return DateFormat.getDateInstance(DateFormat.LONG).format(date)
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

    fun getNoteColor(color: String): Color {
        return when (color) {
            RED -> Color.RED
            GREEN -> Color.GREEN
            YELLOW -> Color.YELLOW
            else -> Color.NONE
        }
    }
}