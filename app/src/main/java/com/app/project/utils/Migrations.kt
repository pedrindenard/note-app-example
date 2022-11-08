package com.app.project.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    val FROM_1_TO_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `note_table_copy` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT NOT NULL, " +
                    "`description` TEXT NOT NULL, " +
                    "`update` TEXT NOT NULL, " +
                    "`color` TEXT NOT NULL, " +
                    "PRIMARY KEY(`id`))"
            )

            database.execSQL("INSERT INTO `note_table_copy` " +
                    "(`id`, `title`, `description`, `update`) " +
                    "SELECT `id`, `title`, `description`, `update` " +
                    "FROM `note_table`"
            )

            database.execSQL("DROP TABLE `note_table`")

            database.execSQL("ALTER TABLE `note_table_copy` RENAME TO `note_table`")
        }
    }
}