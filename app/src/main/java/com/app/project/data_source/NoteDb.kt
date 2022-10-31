package com.app.project.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = false, entities = [NoteEntity::class])
abstract class NoteDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private lateinit var instance: NoteDb

        fun getInstance(context: Context): NoteDb {
            return if (::instance.isInitialized) instance else {
                instance = Room.databaseBuilder(context, NoteDb::class.java, "note.db").build()
                return instance
            }
        }
    }
}