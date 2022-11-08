package com.app.project.feature.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.project.utils.Migrations

@Database(version = 2, exportSchema = false, entities = [NoteEntity::class])
abstract class NoteDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private lateinit var instance: NoteDb

        fun getInstance(context: Context): NoteDb {
            return if (::instance.isInitialized) instance else {
                instance = Room.databaseBuilder(context, NoteDb::class.java, "note.db")
                    .addMigrations(Migrations.FROM_1_TO_2)
                    .build()
                return instance
            }
        }
    }
}