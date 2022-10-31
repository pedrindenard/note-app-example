package com.app.project.feature.data_source

import androidx.room.*

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM note_table")
    suspend fun get(): List<NoteEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: NoteEntity)

    @Update
    suspend fun update(vararg entity: NoteEntity)

    @Delete
    suspend fun delete(vararg entity: NoteEntity)
}