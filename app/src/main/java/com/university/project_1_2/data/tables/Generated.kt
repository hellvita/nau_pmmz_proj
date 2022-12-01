package com.university.project_1_2.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_quotes")
data class Generated(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "anime")
    var anime: String,
    @ColumnInfo(name = "character")
    var character: String,
    @ColumnInfo(name = "quote")
    var quote: String,
    @ColumnInfo(name = "time")
    var time: String
)
